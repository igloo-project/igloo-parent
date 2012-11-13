package fr.openwide.core.commons.util.mime;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public enum MediaType {
	
	// Applications
	APPLICATION_OCTET_STREAM("application/octet-stream"),
	APPLICATION_JAVASCRIPT("application/javascript", "js"),
	APPLICATION_JSON("application/json", "json"),
	APPLICATION_ZIP("application/zip", "zip"),
	APPLICATION_KML("application/vnd.google-earth.kml+xml", "kml"),
	APPLICATION_KMZ("application/vnd.google-earth.kmz", "kmz"),
	APPLICATION_GPX("application/gpx+xml", "gpx"),
	APPLICATION_PDF("application/pdf", "pdf"),
	
	// Applications > MS Office
	APPLICATION_MS_WORD("application/msword", "doc"),
	APPLICATION_MS_EXCEL("application/vnd.ms-excel", "xls"),
	APPLICATION_MS_POWERPOINT("application/vnd.ms-powerpoint", "ppt"),
	
	APPLICATION_OPENXML_POWERPOINT("application/vnd.openxmlformats-officedocument.presentationml.presentation", "pptx"),
	APPLICATION_OPENXML_EXCEL("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", "xlsx"),
	APPLICATION_OPENXML_WORD("application/vnd.openxmlformats-officedocument.wordprocessingml.document", "docx"),
	
	// Applications > Open Office
	APPLICATION_ODF_CHART("application/vnd.oasis.opendocument.chart", "odc"),
	APPLICATION_ODF_TEXT("application/vnd.oasis.opendocument.text", "odt"),
	APPLICATION_ODF_PRESENTATION("application/vnd.oasis.opendocument.presentation", "odp"),
	
	// Text
	TEXT_PLAIN("text/plain", "plain"),
	TEXT_CSS("text/css", "css"),
	TEXT_CSV("text/csv", "csv"),
	TEXT_HTML("text/html", "html", list("htm")),
	TEXT_CALENDAR("text/calendar", "ics"),
	
	// Images
	IMAGE_ANY("image/*"),
	IMAGE_GIF("image/gif", "gif"),
	IMAGE_JPEG("image/jpeg", "jpg", list("jpe", "jpeg")),
	IMAGE_PNG("image/png", "png"),
	IMAGE_TIFF("image/tiff", "tif", list("tiff")),
	IMAGE_X_ICON("image/xicon", "ico"),
	IMAGE_SVG("image/svg+xml", "svg"),
	IMAGE_BMP("image/x-ms-bmp", "bmp"),
	IMAGE_JPEG2000("image/jp2", "jp2"),
	
	// Video
	VIDEO_ANY("video/*"),
	
	// Audio
	AUDIO_ANY("audio/*")
	;

	private String mimeType;
	
	private String primaryExtension;
	
	private List<String> additionalExtensions = new ArrayList<String>();
	
	private static final Map<String, MediaType> MIME_TYPE_MAPPING = new HashMap<String, MediaType>();
	
	private static final Map<String, MediaType> EXTENSION_MAPPING = new HashMap<String, MediaType>();
	
	static {
		for (MediaType mimeType : values()) {
			MIME_TYPE_MAPPING.put(mimeType.mime(), mimeType);
			for (String extension : mimeType.supportedExtensions()) {
				EXTENSION_MAPPING.put(extension, mimeType);
			}
		}
	}
	
	private MediaType(String mimeType) {
		this.mimeType = mimeType;
	}
	
	private MediaType(String mimeType, String primaryExtension) {
		this.mimeType = mimeType;
		this.primaryExtension = primaryExtension;
	}
	
	private MediaType(String mimeType, String primaryExtension, List<String> additionalExtensions) {
		this.mimeType = mimeType;
		this.primaryExtension = primaryExtension;
		this.additionalExtensions = additionalExtensions;
	}
	
	private static List<String> list(String... elements) {
		return new ArrayList<String>(Arrays.asList(elements));
	}
	
	private static String normalize(String string) {
		if (string == null) {
			return null;
		}
		return string.toLowerCase(Locale.ROOT);
	}

	public String mime() {
		return mimeType;
	}
	
	public String mimeUtf8() {
		StringBuilder sb = new StringBuilder(mimeType);
		sb.append(";charset=UTF-8");
		return sb.toString();
	}
	
	public String extension() {
		return primaryExtension;
	}

	public List<String> supportedExtensions() {
		List<String> supportedExtensions = new ArrayList<String>(additionalExtensions);
		
		if (primaryExtension != null) {
			supportedExtensions.add(0, primaryExtension);
		}
		
		return supportedExtensions;
	}
	
	public boolean supports(String extension) {
		if (extension == null) {
			return false;
		}
		return supportedExtensions().contains(normalize(extension));
	}
	
	public static MediaType fromMimeType(String mimeType) {
		if (mimeType == null) {
			return null;
		}
		
		String mimeTypeWithoutParameters;
		int parameterStart = mimeType.indexOf(';');
		if (parameterStart == -1) {
			mimeTypeWithoutParameters = normalize(mimeType);
		} else {
			mimeTypeWithoutParameters = normalize(mimeType.substring(0, parameterStart));
		}
		return MIME_TYPE_MAPPING.get(mimeTypeWithoutParameters);
	}
	
	public static MediaType fromExtension(String extension) {
		return EXTENSION_MAPPING.get(normalize(extension));
	}
}