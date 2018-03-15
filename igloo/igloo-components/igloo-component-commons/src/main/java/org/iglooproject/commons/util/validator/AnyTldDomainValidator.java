package org.iglooproject.commons.util.validator;

import java.io.Serializable;

import org.apache.commons.validator.routines.DomainValidator;
import org.apache.commons.validator.routines.RegexValidator;
import org.iglooproject.functional.SerializablePredicate2;

/**
 * A fixed DomainValidator that allows <em>any</em> TLD, and not only some hard-coded list, since allowed TLDs may vary over time.
 * <p>Regular expressions were taken from {@link DomainValidator}
 */
public final class AnyTldDomainValidator extends RegexValidator implements Serializable {

	private static final long serialVersionUID = 6571374115899151352L;

	static final String DOMAIN_LABEL_REGEX = "[\\p{L}\\p{Digit}](?>[\\p{L}\\p{Digit}_-]*[\\p{L}\\p{Digit}])*";
	static final String TOP_LABEL_REGEX = "\\p{Alpha}{2,}";
	private static final String DOMAIN_NAME_REGEX = "^(?:" + DOMAIN_LABEL_REGEX + "\\.)+" + "(" + TOP_LABEL_REGEX + ")$";
	
	private static final AnyTldDomainValidator INSTANCE = new AnyTldDomainValidator();
	
	public static AnyTldDomainValidator getInstance() {
		return INSTANCE;
	}

	private AnyTldDomainValidator() {
		super(DOMAIN_NAME_REGEX);
	}

	public SerializablePredicate2<String> predicate() {
		return this::isValid;
	}

}
