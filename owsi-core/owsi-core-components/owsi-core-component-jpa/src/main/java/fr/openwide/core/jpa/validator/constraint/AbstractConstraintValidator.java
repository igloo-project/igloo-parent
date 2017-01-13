package fr.openwide.core.jpa.validator.constraint;

import java.lang.annotation.Annotation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public abstract class AbstractConstraintValidator<A extends Annotation, T> implements ConstraintValidator<A, T> {

	@Override
	public boolean isValid(T value, ConstraintValidatorContext context) {
		if (value == null) {
			return true;
		}
		return isValidNotNull(value, context);
	}

	public abstract boolean isValidNotNull(T value, ConstraintValidatorContext context);

}
