package basicapp.front.common.validator;

import org.apache.commons.validator.routines.RegexValidator;
import org.iglooproject.functional.SerializablePredicate2;

public class PostalCodeValidator extends RegexValidator {

  private static final long serialVersionUID = 5254830905190414225L;

  private static final PostalCodeValidator INSTANCE = new PostalCodeValidator();

  private static final String POSTAL_CODE_REGEX = "^[a-zA-Z0-9]*$";

  public static PostalCodeValidator getInstance() {
    return INSTANCE;
  }

  public PostalCodeValidator() {
    super(POSTAL_CODE_REGEX, false);
  }

  public SerializablePredicate2<String> predicate() {
    return this::isValid;
  }
}
