package fr.openwide.core.wicket.more.util.validate.validators;

import org.apache.commons.lang3.Validate;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidator;
import org.apache.wicket.validation.ValidationError;

import com.google.common.base.Predicate;

public class PredicateValidator<T> implements IValidator<T> {
	
	private static final long serialVersionUID = 2351404391379818307L;
	
	private final Predicate<? super T> predicate;
	private final String errorKey;

	public PredicateValidator(Predicate<? super T> predicate, String errorKey) {
		super();
		Validate.notNull(predicate, "predicate must not be null");
		Validate.notEmpty(errorKey, "errorKey must not be empty");
		
		this.predicate = predicate;
		this.errorKey = errorKey;
	}

	public Predicate<? super T> getPredicate() {
		return predicate;
	}

	public String getErrorKey() {
		return errorKey;
	}

	@Override
	public void validate(IValidatable<T> validatable) {
		if (!predicate.apply(validatable.getValue())) {
			validatable.error(decorate(new ValidationError(this)));
		}
	}

	private ValidationError decorate(ValidationError validationError) {
		validationError.addKey(errorKey);
		return validationError;
	}

}
