package basicapp.front.common.validator;

import org.apache.commons.validator.routines.RegexValidator;

public class EmailAddressValidator extends RegexValidator {

  private static final long serialVersionUID = 1L;

  private static final String REGEX = "^[-+.\\w]+@[-.\\w]+\\.[-.\\w]+$";

  private static final EmailAddressValidator INSTANCE = new EmailAddressValidator();

  public static final EmailAddressValidator getInstance() {
    return INSTANCE;
  }

  public EmailAddressValidator() {
    super(REGEX);
  }
}
