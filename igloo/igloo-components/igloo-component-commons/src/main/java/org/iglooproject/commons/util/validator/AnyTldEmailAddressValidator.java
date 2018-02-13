package org.iglooproject.commons.util.validator;

import java.io.Serializable;

import org.apache.commons.validator.routines.EmailValidator;
import org.iglooproject.commons.util.functional.SerializablePredicate;

/**
 * An {@link EmailValidator} relying on {@link AnyTldDomainValidator} for domain validation.
 */
public class AnyTldEmailAddressValidator extends EmailValidator implements Serializable {

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

	public SerializablePredicate<String> predicate() {
		return this::isValid;
	}

}
