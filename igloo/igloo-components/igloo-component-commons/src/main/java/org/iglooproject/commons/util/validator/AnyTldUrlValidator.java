package org.iglooproject.commons.util.validator;

import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.validator.routines.UrlValidator;
import org.iglooproject.functional.SerializablePredicate2;

/**
 * An {@link UrlValidator} relying on {@link AnyTldDomainAndPortValidator} for domain and port validation.
 */
public class AnyTldUrlValidator extends UrlValidator implements Serializable {

	private static final long serialVersionUID = 5254830905190414225L;
	
	private static final AnyTldUrlValidator INSTANCE = new AnyTldUrlValidator();

	/** Copy from UrlValidator. */
	private static final String URL_REGEX = "^(([^:/?#]+):)?(//([^/?#]*))?([^?#]*)(\\?([^#]*))?(#(.*))?";
	/** Copy from UrlValidator. */
	private static final Pattern URL_PATTERN = Pattern.compile(URL_REGEX);

	/**
	 * Schema/Protocol (ie. http:, ftp:, file:, etc).
	 * <p>Copy from UrlValidator.
	 */
	private static final int PARSE_URL_SCHEME = 2;

	/**
	 * Includes hostname/ip and port number.
	 * <p>Copy from UrlValidator.
	 */
	private static final int PARSE_URL_AUTHORITY = 4;

	/** Copy from UrlValidator. */
	private static final int PARSE_URL_PATH = 5;

	/** Copy from UrlValidator. */
	private static final int PARSE_URL_QUERY = 7;

	/** Copy from UrlValidator. */
	private static final int PARSE_URL_FRAGMENT = 9;

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

	public SerializablePredicate2<String> predicate() {
		return this::isValid;
	}
	
	/**
	 * Copy from {@link UrlValidator} to avoid enforcing ascii-only characters, especially on the domain name.
	 */
	@Override
	public boolean isValid(String value) {
		if (value == null) {
			return false;
		}
		
		// Do NOT check ASCII. Domain name (in particular) may include non-ascii parameters.
//		if (!ASCII_PATTERN.matcher(value).matches()) {
//			return false;
//		}
		
		// Check the whole url address structure
		Matcher urlMatcher = URL_PATTERN.matcher(value);
		if (!urlMatcher.matches()) {
			return false;
		}
		
		String scheme = urlMatcher.group(PARSE_URL_SCHEME);
		if (!isValidScheme(scheme)) {
			return false;
		}
		
		String authority = urlMatcher.group(PARSE_URL_AUTHORITY);
		if ("file".equals(scheme) && "".equals(authority)) { // NOSONAR
			// Special case - file: allows an empty authority
		} else {
			// Validate the authority
			if (!isValidAuthority(authority)) {
				return false;
			}
		}
		
		if (!isValidPath(urlMatcher.group(PARSE_URL_PATH))) {
			return false;
		}
		
		if (!isValidQuery(urlMatcher.group(PARSE_URL_QUERY))) {
			return false;
		}
		
		if (!isValidFragment(urlMatcher.group(PARSE_URL_FRAGMENT))) {
			return false;
		}
		
		return true;
	}
}
