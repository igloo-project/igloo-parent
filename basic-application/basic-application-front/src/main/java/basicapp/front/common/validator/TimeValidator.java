package basicapp.front.common.validator;

import org.apache.commons.validator.routines.RegexValidator;

public class TimeValidator extends RegexValidator {

  private static final long serialVersionUID = 1303632566302541081L;

  private static final TimeValidator INSTANCE = new TimeValidator();

  private static final String PATTERN = "^([0-1]{1}[0-9]|2[0-3]):[0-5][0-9]$";

  public static TimeValidator get() {
    return INSTANCE;
  }

  public TimeValidator() {
    super(PATTERN);
  }
}
