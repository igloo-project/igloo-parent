package org.iglooproject.wicket.more.css.scss.util;

import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.text.StrSubstitutor;

import com.google.common.collect.Maps;

public class JSassWebjarUrlMatcher implements Predicate<String> {

	public static final JSassWebjarUrlMatcher INSTANCE = new JSassWebjarUrlMatcher();

	private static final String PROTOCOL_GROUP = "protocol";
	private static final String WEBJAR_GROUP = "webjar";
	private static final String VERSION_GROUP = "version";
	private static final String RESOURCE_PATH_GROUP = "path";

	private final Pattern pattern;

	private JSassWebjarUrlMatcher() {
		super();
		BiFunction<String, String, String> capture = (pattern, name) -> String.format("(?<%s>%s)", name, pattern);
		Map<String, String> patterns = Maps.newHashMap();
		patterns.put(PROTOCOL_GROUP, capture.apply("webjars", PROTOCOL_GROUP));
		patterns.put(WEBJAR_GROUP, capture.apply("[^:/]+", WEBJAR_GROUP));
		patterns.put(VERSION_GROUP,capture.apply("[^/]+", VERSION_GROUP));
		patterns.put(RESOURCE_PATH_GROUP, capture.apply("/.+", RESOURCE_PATH_GROUP));
		StrSubstitutor substitutor = new StrSubstitutor(patterns, "<", ">");
		substitutor.setEnableSubstitutionInVariables(false);
		substitutor.setDisableSubstitutionInValues(true);
		String regexp = substitutor.replace("^<protocol>://<webjar>:?<version>?<path>$");
		pattern = Pattern.compile(regexp);
	}

	@Override
	public boolean test(String t) {
		return pattern.matcher(t).matches();
	}

	/**
	 * @param string to convert to a webjar url
	 * @return a {@link WebjarUrl} or null
	 */
	public WebjarUrl match(String t) {
		Matcher matcher = pattern.matcher(t);
		if (matcher.matches()) {
			return new WebjarUrl(matcher.group(PROTOCOL_GROUP), matcher.group(WEBJAR_GROUP), matcher.group(VERSION_GROUP), matcher.group(RESOURCE_PATH_GROUP));
		} else {
			return null;
		}
	}

	public static class WebjarUrl {
		private final String protocol;
		private final String webjar;
		private final String version;
		private final String resourcePath;
		
		private WebjarUrl(String protocol, String webjar, String version, String resourcePath) {
			super();
			this.protocol = protocol;
			this.webjar = webjar;
			this.version = version;
			this.resourcePath = resourcePath;
		}

		public String getProtocol() {
			return protocol;
		}

		public String getWebjar() {
			return webjar;
		}

		public String getVersion() {
			return version;
		}

		public String getResourcePath() {
			return resourcePath;
		}
	}

}
