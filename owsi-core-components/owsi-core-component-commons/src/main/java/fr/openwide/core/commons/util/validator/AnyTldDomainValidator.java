package fr.openwide.core.commons.util.validator;

import org.apache.commons.validator.routines.DomainValidator;
import org.apache.commons.validator.routines.RegexValidator;

import com.google.common.base.Predicate;

import fr.openwide.core.commons.util.functional.SerializablePredicate;

/**
 * A fixed DomainValidator that allows <em>any</em> TLD, and not only some hard-coded list, since allowed TLDs may vary over time.
 * <p>Regular expressions were taken from {@link DomainValidator}
 */
public final class AnyTldDomainValidator extends RegexValidator implements SerializablePredicate<String> {

	private static final long serialVersionUID = 6571374115899151352L;

	static final String DOMAIN_LABEL_REGEX = "[\\p{L}\\p{Digit}](?>[\\p{L}\\p{Digit}-]*[\\p{L}\\p{Digit}])*";
	static final String TOP_LABEL_REGEX = "\\p{Alpha}{2,}";
	private static final String DOMAIN_NAME_REGEX = "^(?:" + DOMAIN_LABEL_REGEX + "\\.)+" + "(" + TOP_LABEL_REGEX + ")$";
	
	private static final AnyTldDomainValidator INSTANCE = new AnyTldDomainValidator();
	
	public static AnyTldDomainValidator getInstance() {
		return INSTANCE;
	}

	private AnyTldDomainValidator() {
		super(DOMAIN_NAME_REGEX);
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
