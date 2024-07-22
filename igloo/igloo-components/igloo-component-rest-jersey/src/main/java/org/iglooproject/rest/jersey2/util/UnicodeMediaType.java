package org.iglooproject.rest.jersey2.util;

import jakarta.ws.rs.core.MediaType;
import java.util.Collections;
import java.util.Map;

public final class UnicodeMediaType {

  private static final Map<String, String> CHARSET_PARAMETERS =
      Collections.singletonMap("charset", "UTF-8");

  public static final String APPLICATION_XML = "application/xml;charset=UTF-8";
  public static final MediaType APPLICATION_XML_TYPE =
      new MediaType("application", "xml", CHARSET_PARAMETERS);

  public static final String APPLICATION_ATOM_XML = "application/atom+xml;charset=UTF-8";
  public static final MediaType APPLICATION_ATOM_XML_TYPE =
      new MediaType("application", "atom+xml", CHARSET_PARAMETERS);

  public static final String APPLICATION_XHTML_XML = "application/xhtml+xml;charset=UTF-8";
  public static final MediaType APPLICATION_XHTML_XML_TYPE =
      new MediaType("application", "xhtml+xml", CHARSET_PARAMETERS);

  public static final String APPLICATION_SVG_XML = "application/svg+xml;charset=UTF-8";
  public static final MediaType APPLICATION_SVG_XML_TYPE =
      new MediaType("application", "svg+xml", CHARSET_PARAMETERS);

  public static final String APPLICATION_JSON = "application/json;charset=UTF-8";
  public static final MediaType APPLICATION_JSON_TYPE =
      new MediaType("application", "json", CHARSET_PARAMETERS);

  public static final String TEXT_PLAIN = "text/plain;charset=UTF-8";
  public static final MediaType TEXT_PLAIN_TYPE =
      new MediaType("text", "plain", CHARSET_PARAMETERS);

  public static final String TEXT_XML = "text/xml;charset=UTF-8";
  public static final MediaType TEXT_XML_TYPE = new MediaType("text", "xml", CHARSET_PARAMETERS);

  public static final String TEXT_HTML = "text/html;charset=UTF-8";
  public static final MediaType TEXT_HTML_TYPE = new MediaType("text", "html", CHARSET_PARAMETERS);

  private UnicodeMediaType() {}
}
