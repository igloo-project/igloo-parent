package org.iglooproject.spring.notification.service.impl;

import com.google.common.collect.Maps;
import java.util.Locale;
import java.util.Map;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.function.Failable;
import org.iglooproject.mail.api.INotificationRecipient;
import org.iglooproject.spring.notification.exception.NotificationContentRenderingException;
import org.iglooproject.spring.notification.model.INotificationContentBody;
import org.iglooproject.spring.notification.model.INotificationContentDescriptor;
import org.iglooproject.spring.notification.model.NotificationContentBody;
import org.iglooproject.spring.property.service.IPropertyService;
import org.springframework.beans.factory.annotation.Autowired;

public class ExplicitelyDefinedNotificationContentDescriptorImpl
    implements INotificationContentDescriptor {

  private final Locale defaultLocale;

  private final Map<Locale, String> subjectByLocale = Maps.newHashMap();

  private final Map<Locale, String> bodyHtmlTextByLocale = Maps.newHashMap();

  private final Map<Locale, String> bodyPlainTextByLocale = Maps.newHashMap();

  @Autowired private IPropertyService propertyService;

  public ExplicitelyDefinedNotificationContentDescriptorImpl(Locale defaultLocale) {
    super();
    this.defaultLocale = defaultLocale;
  }

  @Override
  public String renderSubject() {
    return renderSubject(defaultLocale);
  }

  private String renderSubject(Locale locale) {
    String subject = subjectByLocale.get(locale);
    if (subject == null) {
      subject = subjectByLocale.get(null);
    }
    return subject;
  }

  /** If locale == null, the given subject will be used for any unreferenced locale */
  public void setSubject(Locale locale, String subject) {
    subjectByLocale.put(locale, subject);
  }

  @Override
  public INotificationContentBody renderBody() {
    return renderBody(defaultLocale);
  }

  public INotificationContentBody renderBody(Locale locale) {
    return NotificationContentBody.start()
        .with(
            Failable.asConsumer(
                o -> {
                  o.setPlainText(renderBodyPlainText(locale));
                  o.setHtmlText(renderBodyHtmlText(locale));
                }))
        .build();
  }

  private String renderBodyHtmlText(Locale locale) {
    String body = bodyHtmlTextByLocale.get(locale);
    if (body == null) {
      body = bodyHtmlTextByLocale.get(null);
    }
    return body;
  }

  /** If locale == null, the given body will be used for any unreferenced locale */
  public void setHtmlBody(Locale locale, String body) {
    bodyHtmlTextByLocale.put(locale, body);
  }

  private String renderBodyPlainText(Locale locale) {
    String body = bodyPlainTextByLocale.get(locale);
    if (body == null) {
      body = bodyPlainTextByLocale.get(null);
    }
    return body;
  }

  /** If locale == null, the given body will be used for any unreferenced locale */
  public void setTextBody(Locale locale, String body) {
    bodyPlainTextByLocale.put(locale, body);
  }

  @Override
  public INotificationContentDescriptor withContext(INotificationRecipient recipient) {
    Locale recipientLocale = propertyService.toAvailableLocale(recipient.getLocale());
    return new Wrapper(this, recipientLocale);
  }

  private static class Wrapper implements INotificationContentDescriptor {

    private final ExplicitelyDefinedNotificationContentDescriptorImpl wrapped;

    private final Locale locale;

    public Wrapper(ExplicitelyDefinedNotificationContentDescriptorImpl wrapped, Locale locale) {
      super();
      this.wrapped = wrapped;
      this.locale = locale;
    }

    @Override
    public boolean equals(Object obj) {
      if (obj instanceof Wrapper) {
        if (obj == this) {
          return true;
        }
        Wrapper other = (Wrapper) obj;
        return new EqualsBuilder()
            .append(wrapped, other.wrapped)
            .append(locale, other.locale)
            .build();
      }
      return false;
    }

    @Override
    public int hashCode() {
      return new HashCodeBuilder().append(wrapped).append(locale).build();
    }

    @Override
    public String renderSubject() throws NotificationContentRenderingException {
      return wrapped.renderSubject(locale);
    }

    @Override
    public INotificationContentBody renderBody() throws NotificationContentRenderingException {
      return wrapped.renderBody(locale);
    }

    @Override
    public INotificationContentDescriptor withContext(INotificationRecipient recipient) {
      return wrapped.withContext(recipient);
    }
  }
}
