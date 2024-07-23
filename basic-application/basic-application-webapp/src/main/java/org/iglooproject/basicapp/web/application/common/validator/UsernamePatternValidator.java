package org.iglooproject.basicapp.web.application.common.validator;

import java.util.Collections;
import java.util.regex.Pattern;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidationError;
import org.apache.wicket.validation.ValidationError;
import org.apache.wicket.validation.validator.PatternValidator;

public class UsernamePatternValidator extends PatternValidator {

  private static final long serialVersionUID = 1303632566302541081L;

  private static final String REGEX = "[A-Za-z0-9\\-\\_@.]+";

  private static final Pattern PATTERN = Pattern.compile(REGEX);

  private static final UsernamePatternValidator INSTANCE = new UsernamePatternValidator();

  public UsernamePatternValidator() {
    super(PATTERN);
  }

  @Override
  protected IValidationError decorate(IValidationError error, IValidatable<String> validatable) {
    super.decorate(error, validatable);

    ((ValidationError) error)
        .setKeys(Collections.singletonList("common.validator.username.pattern"));
    return error;
  }

  public static UsernamePatternValidator get() {
    return INSTANCE;
  }
}
