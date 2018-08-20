package org.iglooproject.commons.util.validator;

import java.io.Serializable;

import org.apache.commons.validator.routines.DomainValidator;
import org.apache.commons.validator.routines.RegexValidator;
import org.iglooproject.functional.SerializablePredicate2;

/**
 * A fixed "authority" validator that allows <em>any</em> TLD, and not only some hard-coded list, since allowed TLDs may vary over time.
 * <p>Regular expressions were taken from {@link DomainValidator}
 */
public final class AnyTldDomainAndPortValidator extends RegexValidator implements Serializable {

	private static final long serialVersionUID = -8166436754268661547L;
	
	private static final String PORT_REGEX = "\\d+";
	private static final String DOMAIN_NAME_AND_PORT_REGEX = "^(?:" + AnyTldDomainValidator.DOMAIN_LABEL_REGEX + "\\.)+" + "(" + AnyTldDomainValidator.TOP_LABEL_REGEX + ")" + "(\\:" + PORT_REGEX + ")?" + "$";
	
	private static final AnyTldDomainAndPortValidator INSTANCE = new AnyTldDomainAndPortValidator();
	
	public static AnyTldDomainAndPortValidator getInstance() {
		return INSTANCE;
	}

	private AnyTldDomainAndPortValidator() {
		super(DOMAIN_NAME_AND_PORT_REGEX);
	}

	public SerializablePredicate2<String> predicate() {
		return this::isValid;
	}

}
