package basicapp.front.common.converter;

import basicapp.back.business.common.model.EmailAddress;
import basicapp.front.common.validator.EmailAddressValidator;
import java.util.Locale;
import org.apache.wicket.util.convert.ConversionException;
import org.apache.wicket.util.convert.converter.AbstractConverter;
import org.springframework.util.StringUtils;

public final class EmailAddressConverter extends AbstractConverter<EmailAddress> {

  private static final long serialVersionUID = 1L;

  private static final EmailAddressConverter INSTANCE = new EmailAddressConverter();

  public static EmailAddressConverter get() {
    return INSTANCE;
  }

  @Override
  public EmailAddress convertToObject(String value, Locale locale) throws ConversionException {
    String valueClean = StringUtils.trimAllWhitespace(value);
    if (!StringUtils.hasText(valueClean)) {
      return null;
    }
    if (!EmailAddressValidator.getInstance().isValid(valueClean)) {
      throw newConversionException("Invalid email address format", value, locale)
          .setResourceKey("common.validator.emailAddress");
    }
    return new EmailAddress(valueClean);
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
