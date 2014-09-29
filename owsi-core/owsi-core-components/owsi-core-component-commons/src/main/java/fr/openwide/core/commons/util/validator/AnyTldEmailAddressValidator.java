package fr.openwide.core.commons.util.validator;

import org.apache.commons.validator.routines.EmailValidator;

import com.google.common.base.Predicate;

import fr.openwide.core.commons.util.functional.SerializablePredicate;

/**
 * An {@link EmailValidator} relying on {@link AnyTldDomainValidator} for domain validation.
 */
public class AnyTldEmailAddressValidator extends EmailValidator implements SerializablePredicate<String> {

	private static final long serialVersionUID = 3764517887042593145L;
	
	private static final AnyTldEmailAddressValidator INSTANCE = new AnyTldEmailAddressValidator(false);
	
	private static final AnyTldEmailAddressValidator INSTANCE_WITH_LOCAL = new AnyTldEmailAddressValidator(true);
	
	public static AnyTldEmailAddressValidator getInstance() {
		return INSTANCE;
	}
	
	public static AnyTldEmailAddressValidator getInstance(boolean allowLocal) {
		if (allowLocal) {
			return INSTANCE_WITH_LOCAL;
		} else {
			return INSTANCE;
		}
	}

	protected AnyTldEmailAddressValidator(boolean allowLocal) {
		super(allowLocal);
	}

	@Override
	protected boolean isValidDomain(String domain) {
		return AnyTldDomainValidator.getInstance().isValid(domain);
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
