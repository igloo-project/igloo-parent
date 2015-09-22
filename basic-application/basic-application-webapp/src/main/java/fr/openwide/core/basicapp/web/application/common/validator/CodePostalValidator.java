package fr.openwide.core.basicapp.web.application.common.validator;

import org.apache.commons.validator.routines.RegexValidator;

import com.google.common.base.Predicate;

import fr.openwide.core.commons.util.functional.SerializablePredicate;

public class CodePostalValidator extends RegexValidator implements SerializablePredicate<String> {

	private static final long serialVersionUID = 5254830905190414225L;
	
	private static final CodePostalValidator INSTANCE = new CodePostalValidator();
	
	private static final String POSTAL_CODE_REGEX = "^[a-zA-Z0-9]*$";
	
	public static CodePostalValidator getInstance() {
		return INSTANCE;
	}

	public CodePostalValidator() {
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