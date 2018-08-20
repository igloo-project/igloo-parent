package org.iglooproject.functional.converter;

import java.net.URI;
import java.net.URISyntaxException;

public class StringURIConverter extends SerializableConverter2<String, URI> {

	private static final long serialVersionUID = -3571532814668198117L;

	private static final StringURIConverter INSTANCE = new StringURIConverter();

	public static StringURIConverter get() {
		return INSTANCE;
	}

	protected StringURIConverter() {
	}

	@Override
	protected URI doForward(String a) {
		try {
			return new URI(a);
		} catch (URISyntaxException e) {
			throw new IllegalStateException(String.format("Property '%1s' is not a valid URI", a), e);
		}
	}

	@Override
	protected String doBackward(URI b) {
		return b.toString();
	}

	/**
	 * Workaround sonar/findbugs - https://github.com/google/guava/issues/1858
	 * Guava Converter overrides only equals to add javadoc, but findbugs warns about non coherent equals/hashcode
	 * possible issue.
	 */
	@Override
	public boolean equals(Object object) {
		return super.equals(object);
	}

	/**
	 * Workaround sonar/findbugs - see #equals(Object)
	 */
	@Override
	public int hashCode() {
		return super.hashCode();
	}

}
