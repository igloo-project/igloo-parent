package basicapp.front.common.validator;

import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidator;
import org.apache.wicket.validation.ValidationError;

public class EmailAddressValueValidator implements IValidator<String> {

  private static final long serialVersionUID = 1L;

  private static final EmailAddressValidator VALIDATOR = new EmailAddressValidator();

  private static final EmailAddressValueValidator INSTANCE = new EmailAddressValueValidator();

  public static EmailAddressValueValidator get() {
    return INSTANCE;
  }

  protected EmailAddressValueValidator() {
    super();
  }

  @Override
  public void validate(IValidatable<String> validatable) {
    if (!VALIDATOR.isValid(validatable.getValue())) {
      validatable.error(new ValidationError().addKey("common.validator.emailAddress"));
    }
  }
}
