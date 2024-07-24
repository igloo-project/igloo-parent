package igloo.wicket.component;

import java.math.BigDecimal;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.ValidationError;
import org.apache.wicket.validation.validator.RangeValidator;

public class PercentageValidator extends RangeValidator<BigDecimal> {
  private static final long serialVersionUID = 715451828147101142L;

  private static final PercentageValidator DEFAULT_PERCENTAGE_AS_RATIO_VALIDATOR =
      new PercentageValidator(new BigDecimal("0"), new BigDecimal("1"), true);

  private static final PercentageValidator DEFAULT_PERCENTAGE_VALIDATOR =
      new PercentageValidator(new BigDecimal("0"), new BigDecimal("100"), true);

  private boolean decimal;

  public PercentageValidator(BigDecimal minimumValue, BigDecimal maximumValue, boolean decimal) {
    super(minimumValue, maximumValue);
    this.decimal = decimal;
  }

  @Override
  public void validate(IValidatable<BigDecimal> validatable) {
    BigDecimal percentage = validatable.getValue();

    if (percentage != null) {
      final BigDecimal min = getMinimum();
      final BigDecimal max = getMaximum();
      if (!decimal && percentage.scale() != 0) {
        ValidationError error = new ValidationError();
        error.addKey("common.validator.percentage.decimal.error");
        validatable.error(error);
      }
      if (percentage.compareTo(min) < 0 || percentage.compareTo(max) > 0) {
        ValidationError error = new ValidationError();
        error.getVariables().put("min", min);
        error.getVariables().put("max", max);
        error.addKey("common.validator.percentage.range.error");
        validatable.error(error);
      }
    }
  }

  public static PercentageValidator getDefaultPercentageAsRatioValidator() {
    return DEFAULT_PERCENTAGE_AS_RATIO_VALIDATOR;
  }

  public static PercentageValidator getDefaultPercentageValidator() {
    return DEFAULT_PERCENTAGE_VALIDATOR;
  }
}
