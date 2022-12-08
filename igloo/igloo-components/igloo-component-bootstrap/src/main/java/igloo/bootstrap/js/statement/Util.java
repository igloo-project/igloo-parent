package igloo.bootstrap.js.statement;

import java.util.regex.Pattern;

class Util {

	private Util() {}

	private static final Pattern PATTERN = Pattern.compile("[a-zA-Z_$][a-zA-Z0-9_$]*");

	public static String escapeIdentifier(String identifier) {
		if (!PATTERN.matcher(identifier).matches()) {
			return escape(identifier);
		} else {
			return identifier;
		}
	}

	public static String escape(String value) {
		return "\"" + value.replace("\"", "\\\"") +  "\"";
	}

}
