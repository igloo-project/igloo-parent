package basicapp.front.common.validator;

import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidator;
import org.apache.wicket.validation.ValidationError;

public class EmailAddressValueValidator implements IValidator<String> {

  private static final long serialVersionUID = 1L;

  private static final EmailAddressValueValidator INSTANCE = new EmailAddressValueValidator();

  public static EmailAddressValueValidator get() {
    return INSTANCE;
  }

  @Override
  public void validate(IValidatable<String> validatable) {
    String value = validatable.getValue();

    if (!EmailAddressValidator.getInstance().isValid(value)) {
      validatable.error(new ValidationError().addKey("common.validator.emailAddress"));
      return;
    }

    try {
      InternetAddress internetAddress = new InternetAddress(value);
      internetAddress.validate();
    } catch (AddressException e) {
      validatable.error(new ValidationError().addKey("common.validator.emailAddress"));
    }
  }
}
