package org.iglooproject.spring.notification.service.impl;

import com.google.common.collect.Maps;
import java.util.Locale;
import java.util.Map;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.iglooproject.mail.api.INotificationRecipient;
import org.iglooproject.spring.notification.exception.NotificationContentRenderingException;
import org.iglooproject.spring.notification.model.INotificationContentDescriptor;
import org.iglooproject.spring.property.service.IPropertyService;
import org.springframework.beans.factory.annotation.Autowired;

public class ExplicitelyDefinedNotificationContentDescriptorImpl
    implements INotificationContentDescriptor {

  private final Locale defaultLocale;

  private final Map<Locale, String> subjectByLocale = Maps.newHashMap();

  private final Map<Locale, String> htmlBodyByLocale = Maps.newHashMap();

  private final Map<Locale, String> textBodyByLocale = Maps.newHashMap();

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
    String htmlBody = subjectByLocale.get(locale);
    if (htmlBody == null) {
      htmlBody = subjectByLocale.get(null);
    }
    return htmlBody;
  }

  /** If locale == null, the given subject will be used for any unreferenced locale */
  public void setSubject(Locale locale, String subject) {
    subjectByLocale.put(locale, subject);
  }

  @Override
  public String renderHtmlBody() {
    return renderHtmlBody(defaultLocale);
  }

  private String renderHtmlBody(Locale locale) {
    String body = htmlBodyByLocale.get(locale);
    if (body == null) {
      body = htmlBodyByLocale.get(null);
    }
    return body;
  }

  /** If locale == null, the given body will be used for any unreferenced locale */
  public void setHtmlBody(Locale locale, String body) {
    htmlBodyByLocale.put(locale, body);
  }

  @Override
  public String renderTextBody() {
    return renderTextBody(defaultLocale);
  }

  private String renderTextBody(Locale locale) {
    String body = textBodyByLocale.get(locale);
    if (body == null) {
      body = textBodyByLocale.get(null);
    }
    return body;
  }

  /** If locale == null, the given body will be used for any unreferenced locale */
  public void setTextBody(Locale locale, String body) {
    textBodyByLocale.put(locale, body);
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
    public String renderHtmlBody() throws NotificationContentRenderingException {
      return wrapped.renderHtmlBody(locale);
    }

    @Override
    public String renderTextBody() throws NotificationContentRenderingException {
      return wrapped.renderTextBody(locale);
    }

    @Override
    public INotificationContentDescriptor withContext(INotificationRecipient recipient) {
      return wrapped.withContext(recipient);
    }
  }
}
