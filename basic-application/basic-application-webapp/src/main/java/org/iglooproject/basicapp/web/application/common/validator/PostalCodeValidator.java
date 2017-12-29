package org.iglooproject.basicapp.web.application.common.validator;

import org.apache.commons.validator.routines.RegexValidator;

import com.google.common.base.Predicate;

import org.iglooproject.commons.util.functional.SerializablePredicate;

public class PostalCodeValidator extends RegexValidator implements SerializablePredicate<String> {

	private static final long serialVersionUID = 5254830905190414225L;
	
	private static final PostalCodeValidator INSTANCE = new PostalCodeValidator();
	
	private static final String POSTAL_CODE_REGEX = "^[a-zA-Z0-9]*$";
	
	public static PostalCodeValidator getInstance() {
		return INSTANCE;
	}

	public PostalCodeValidator() {
		super(POSTAL_CODE_REGEX, false);
	}
	
	/**
	 * @deprecated Provided only to satisfy the {@link Predicate} interface; use
	 *             {@link #isValid(String)} instead.
	 */
	@Deprecated
	@Override
	public boolean apply(String input) {
		return isValid(input);
	}

}