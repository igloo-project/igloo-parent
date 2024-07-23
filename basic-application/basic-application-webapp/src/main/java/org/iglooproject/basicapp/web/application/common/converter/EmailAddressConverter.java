package org.iglooproject.basicapp.web.application.common.converter;

import java.util.Locale;
import org.apache.wicket.util.convert.ConversionException;
import org.apache.wicket.util.convert.converter.AbstractConverter;
import org.iglooproject.basicapp.core.business.common.model.EmailAddress;
import org.iglooproject.commons.util.validator.AnyTldEmailAddressValidator;
import org.springframework.util.StringUtils;

public final class EmailAddressConverter extends AbstractConverter<EmailAddress> {

  private static final long serialVersionUID = -2929883038769154563L;

  @Override
  public EmailAddress convertToObject(String value, Locale locale) throws ConversionException {
    String trimmedValue = StringUtils.trimAllWhitespace(value);
    if (StringUtils.hasText(trimmedValue)) {
      if (!AnyTldEmailAddressValidator.getInstance().isValid(trimmedValue)) {
        throw newConversionException("Invalid email format", value, locale)
            .setResourceKey("common.validator.email");
      }
      return new EmailAddress(trimmedValue);
    }
    return null;
  }

  @Override
  public String convertToString(EmailAddress value, Locale locale) {
    if (value == null) {
      return null;
    }
    return value.getValue();
  }

  @Override
  protected Class<EmailAddress> getTargetType() {
    return EmailAddress.class;
  }
}
