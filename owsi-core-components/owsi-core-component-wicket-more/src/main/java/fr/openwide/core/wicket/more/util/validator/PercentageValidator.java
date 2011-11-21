package fr.openwide.core.wicket.more.util.validator;

import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.ValidationError;
import org.apache.wicket.validation.validator.RangeValidator;

import fr.openwide.core.wicket.more.util.NumberAdapter;

public class PercentageValidator<N extends Number & Comparable<N>> extends RangeValidator<N> {
	private static final long serialVersionUID = 715451828147101142L;

	public PercentageValidator(NumberAdapter<N> numberAdapter) {
		super(numberAdapter.convert(0), numberAdapter.convert(100));
	}

	public PercentageValidator(N minimumValue, N maximumValue) {
		super(minimumValue, maximumValue);
	}

	@Override
	public void validate(IValidatable<N> validatable) {
		N percentage = validatable.getValue();
		final N min = getMinimum();
		final N max = getMaximum();
		if (percentage.compareTo(min) < 0 || percentage.compareTo(max) > 0)
		{
			ValidationError error = new ValidationError();
			error.addMessageKey("percentageValidator.error");
			validatable.error(error);
		}
	}
}
