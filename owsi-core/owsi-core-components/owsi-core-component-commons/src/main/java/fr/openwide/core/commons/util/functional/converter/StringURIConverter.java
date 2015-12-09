package fr.openwide.core.commons.util.functional.converter;

import java.net.URI;
import java.net.URISyntaxException;

import com.google.common.base.Converter;

public class StringURIConverter extends Converter<String, URI> {

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

}
