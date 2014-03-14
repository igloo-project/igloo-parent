package fr.openwide.core.commons.util.validator;

import org.apache.commons.validator.routines.RegexValidator;

import com.google.common.base.Predicate;

import fr.openwide.core.commons.util.functional.SerializablePredicate;

/**
 * An {@link RegexValidator} for phone number validation: it's very permissive but should avoid most of the errors.
 */
public class PermissivePhoneNumberValidator extends RegexValidator implements SerializablePredicate<String> {

	private static final long serialVersionUID = 5254830905190414225L;
	
	private static PermissivePhoneNumberValidator INSTANCE = new PermissivePhoneNumberValidator();
	
	private static final String PHONE_NUMBER_REGEX = "^\\+?[0-9 \\-\\.()]+$";
	
	public static PermissivePhoneNumberValidator getInstance() {
		return INSTANCE;
	}

	public PermissivePhoneNumberValidator() {
		super(PHONE_NUMBER_REGEX, false);
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
