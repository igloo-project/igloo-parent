package org.iglooproject.commons.util.validator;

import java.io.Serializable;

import org.apache.commons.validator.routines.RegexValidator;
import org.iglooproject.functional.SerializablePredicate2;

/**
 * An {@link RegexValidator} for phone number validation: it's very permissive but should avoid most of the errors.
 */
public class PermissivePhoneNumberValidator extends RegexValidator implements Serializable {

	private static final long serialVersionUID = 5254830905190414225L;
	
	private static final PermissivePhoneNumberValidator INSTANCE = new PermissivePhoneNumberValidator();
	
	private static final String PHONE_NUMBER_REGEX = "^\\+?[0-9 \\-\\.()]+$";
	
	public static PermissivePhoneNumberValidator getInstance() {
		return INSTANCE;
	}

	public PermissivePhoneNumberValidator() {
		super(PHONE_NUMBER_REGEX, false);
	}

	public SerializablePredicate2<String> predicate() {
		return this::isValid;
	}

}
