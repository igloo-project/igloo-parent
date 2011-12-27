package fr.openwide.core.rest.jersey.util;

import java.util.Collections;
import java.util.Map;

import javax.ws.rs.core.MediaType;

public class UnicodeMediaType {
	
	private final static Map<String, String> CHARSET_PARAMETERS = Collections.singletonMap("charset", "UTF-8");

	public final static String APPLICATION_XML = "application/xml;charset=UTF-8";
	public final static MediaType APPLICATION_XML_TYPE = new MediaType("application", "xml", CHARSET_PARAMETERS);

	public final static String APPLICATION_ATOM_XML = "application/atom+xml;charset=UTF-8";
	public final static MediaType APPLICATION_ATOM_XML_TYPE = new MediaType("application", "atom+xml", CHARSET_PARAMETERS);

	public final static String APPLICATION_XHTML_XML = "application/xhtml+xml;charset=UTF-8";
	public final static MediaType APPLICATION_XHTML_XML_TYPE = new MediaType("application", "xhtml+xml", CHARSET_PARAMETERS);

	public final static String APPLICATION_SVG_XML = "application/svg+xml;charset=UTF-8";
	public final static MediaType APPLICATION_SVG_XML_TYPE = new MediaType("application", "svg+xml", CHARSET_PARAMETERS);

	public final static String APPLICATION_JSON = "application/json;charset=UTF-8";
	public final static MediaType APPLICATION_JSON_TYPE = new MediaType("application", "json", CHARSET_PARAMETERS);

	public final static String TEXT_PLAIN = "text/plain;charset=UTF-8";
	public final static MediaType TEXT_PLAIN_TYPE = new MediaType("text", "plain", CHARSET_PARAMETERS);

	public final static String TEXT_XML = "text/xml;charset=UTF-8";
	public final static MediaType TEXT_XML_TYPE = new MediaType("text", "xml", CHARSET_PARAMETERS);

	public final static String TEXT_HTML = "text/html;charset=UTF-8";
	public final static MediaType TEXT_HTML_TYPE = new MediaType("text", "html", CHARSET_PARAMETERS);

}
