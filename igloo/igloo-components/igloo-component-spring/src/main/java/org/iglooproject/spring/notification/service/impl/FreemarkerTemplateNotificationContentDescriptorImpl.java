package org.iglooproject.spring.notification.service.impl;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
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
import org.javatuples.LabelValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.util.Assert;

public class FreemarkerTemplateNotificationContentDescriptorImpl
    implements INotificationContentDescriptor {

  private static final Logger LOGGER =
      LoggerFactory.getLogger(FreemarkerTemplateNotificationContentDescriptorImpl.class);

  private static final String ATTACHMENT_NAMES_VARIABLE_NAME = "attachments";

  private final Configuration templateConfiguration;

  private final String templateKey;

  private final Map<Locale, HashMap<String, Object>> templateVariablesByLocale = new HashMap<>();

  private final Collection<LabelValue<String, File>> attachments;

  private final Locale defaultLocale;

  @Autowired private IPropertyService propertyService;

  public FreemarkerTemplateNotificationContentDescriptorImpl(
      Configuration templateConfiguration,
      String templateKey,
      Collection<LabelValue<String, File>> attachments,
      Locale defaultLocale) {
    super();
    this.templateConfiguration = checkNotNull(templateConfiguration);
    this.templateKey = checkNotNull(templateKey);
    this.attachments = checkNotNull(attachments);
    this.defaultLocale = defaultLocale;
  }

  @Override
  public String renderSubject() throws NotificationContentRenderingException {
    return renderSubject(defaultLocale);
  }

  private String renderSubject(Locale locale) throws NotificationContentRenderingException {
    try {
      return getMailElement(MailElement.SUBJECT, locale);
    } catch (TemplateException | IOException e) {
      throw new NotificationContentRenderingException(
          "Exception while rendering notification subject from freemarker template", e);
    }
  }

  @Override
  public INotificationContentBody renderBody() throws NotificationContentRenderingException {
    return NotificationContentBody.start()
        .with(Failable.asConsumer(o -> o.setPlainText(renderBodyPlainText(defaultLocale))))
        .build();
  }

  private String renderBodyPlainText(Locale locale) throws NotificationContentRenderingException {
    try {
      return getMailElement(MailElement.BODY_TEXT, locale);
    } catch (TemplateException | IOException e) {
      throw new NotificationContentRenderingException(
          "Exception while rendering notification body from freemarker template", e);
    }
  }

  @Override
  public INotificationContentDescriptor withContext(INotificationRecipient recipient) {
    Locale recipientLocale = propertyService.toAvailableLocale(recipient.getLocale());
    return new Wrapper(this, recipientLocale);
  }

  private static class Wrapper implements INotificationContentDescriptor {

    private final FreemarkerTemplateNotificationContentDescriptorImpl wrapped;

    private final Locale locale;

    public Wrapper(FreemarkerTemplateNotificationContentDescriptorImpl wrapped, Locale locale) {
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
      return NotificationContentBody.start()
          .with(Failable.asConsumer(o -> o.setPlainText(wrapped.renderBodyPlainText(locale))))
          .build();
    }

    @Override
    public INotificationContentDescriptor withContext(INotificationRecipient recipient) {
      return wrapped.withContext(recipient);
    }
  }

  /**
   * If locale == null, the variable will be considered as not locale-sensitive and will be
   * available for all locales.
   */
  public void setVariable(Locale locale, String name, Object value) {
    Assert.hasText(name, "Variable name must contain text");
    if (!templateVariablesByLocale.containsKey(locale)) {
      templateVariablesByLocale.put(locale, new HashMap<String, Object>());
    }
    templateVariablesByLocale.get(locale).put(name, value);
  }

  /**
   * If locale == null, the variables will be considered as not locale-sensitive and will be
   * available for all locales.
   */
  public void setVariables(Locale locale, Map<String, ?> variables) {
    if (!templateVariablesByLocale.containsKey(locale)) {
      templateVariablesByLocale.put(locale, new HashMap<String, Object>());
    }
    templateVariablesByLocale.get(locale).putAll(variables);
  }

  private Map<String, Object> getTemplateVariables(Locale locale) {
    Map<String, Object> templateVariables = Maps.newHashMap();

    Map<String, Object> sharedVariables = templateVariablesByLocale.get(null);
    if (sharedVariables != null) {
      templateVariables.putAll(sharedVariables);
    }

    Map<String, Object> localeDependentVariables = templateVariablesByLocale.get(locale);
    if (localeDependentVariables != null) {
      templateVariables.putAll(localeDependentVariables);
    }

    if (!attachments.isEmpty()) {
      if (!templateVariables.containsKey(ATTACHMENT_NAMES_VARIABLE_NAME)) {
        Collection<String> labels = Lists.newArrayList();
        for (LabelValue<String, ?> labelValue : attachments) {
          labels.add(labelValue.getLabel());
        }
        templateVariables.put(ATTACHMENT_NAMES_VARIABLE_NAME, labels);
      } else {
        LOGGER.warn(
            ATTACHMENT_NAMES_VARIABLE_NAME + " already present in the map. We don't override it.");
      }
    }

    return templateVariables;
  }

  private String getMailElement(MailElement element, Locale locale)
      throws IOException, TemplateException {
    Map<String, Object> freemarkerModelMap = getTemplateVariables(locale);
    if (freemarkerModelMap.containsKey(element.toString())) {
      throw new IllegalStateException(
          String.format(
              "Variable name '%1$s' is reserved to implementation purposes. Users cannot use it.",
              element));
    }
    freemarkerModelMap.put(element.toString(), true);
    return FreeMarkerTemplateUtils.processTemplateIntoString(
        templateConfiguration.getTemplate(templateKey, locale), freemarkerModelMap);
  }

  private enum MailElement {
    BODY_TEXT,
    SUBJECT
  }
}
