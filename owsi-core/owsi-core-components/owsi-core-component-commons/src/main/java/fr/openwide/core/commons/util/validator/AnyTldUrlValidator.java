package fr.openwide.core.commons.util.validator;

import org.apache.commons.validator.routines.UrlValidator;

import com.google.common.base.Predicate;

import fr.openwide.core.commons.util.functional.SerializablePredicate;

/**
 * An {@link UrlValidator} relying on {@link AnyTldDomainAndPortValidator} for domain and port validation.
 */
public class AnyTldUrlValidator extends UrlValidator implements SerializablePredicate<String> {

	private static final long serialVersionUID = 5254830905190414225L;
	
	private static AnyTldUrlValidator INSTANCE = new AnyTldUrlValidator();
	
	public static AnyTldUrlValidator getInstance() {
		return INSTANCE;
	}

	public AnyTldUrlValidator() {
		super(AnyTldDomainAndPortValidator.getInstance(), 0L);
	}

	public AnyTldUrlValidator(long options) {
		super(AnyTldDomainAndPortValidator.getInstance(), options);
	}

	public AnyTldUrlValidator(String[] schemes, long options) {
		super(schemes, AnyTldDomainAndPortValidator.getInstance(), options);
	}

	public AnyTldUrlValidator(String[] schemes) {
		super(schemes, AnyTldDomainAndPortValidator.getInstance(), 0L);
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
