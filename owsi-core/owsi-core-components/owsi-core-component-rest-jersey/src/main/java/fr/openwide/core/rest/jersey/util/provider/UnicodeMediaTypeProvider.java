package fr.openwide.core.rest.jersey.util.provider;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.MediaType;

import com.sun.jersey.core.header.reader.HttpHeaderReader;
import com.sun.jersey.core.impl.provider.header.MediaTypeProvider;

public class UnicodeMediaTypeProvider extends MediaTypeProvider {
	
	private static final String CHARSET_PARAMETER = "charset";
	
	private static final String UTF8_ENCODING = "UTF-8";

	@Override
	public MediaType fromString(String header) {
		if (header == null) {
			throw new IllegalArgumentException("Media type is null");
		}

		try {
			return valueOf(HttpHeaderReader.newInstance(header));
		} catch (ParseException ex) {
			throw new IllegalArgumentException("Error parsing media type '" + header + "'", ex);
		}
	}

	public static MediaType valueOf(HttpHeaderReader reader) throws ParseException {
		MediaType mediaType = MediaTypeProvider.valueOf(reader);
		
		if (mediaType.getParameters().containsKey(CHARSET_PARAMETER)) {
			return mediaType;
		} else {
			Map<String, String> parameters = new HashMap<String, String>(mediaType.getParameters());
			parameters.put(CHARSET_PARAMETER, UTF8_ENCODING);
			
			MediaType unicodeMediaType = new MediaType(mediaType.getType(), mediaType.getSubtype(), parameters);
			
			return unicodeMediaType;
		}
	}

}