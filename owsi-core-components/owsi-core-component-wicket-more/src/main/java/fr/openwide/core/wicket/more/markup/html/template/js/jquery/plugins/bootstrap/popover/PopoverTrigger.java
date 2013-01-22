package fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.bootstrap.popover;

import java.util.Locale;

public enum PopoverTrigger {

	CLICK("click"),
	HOVER("hover"),
	FOCUS("focus"),
	MANUAL("manual");
	
	private String value;
	
	private PopoverTrigger(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}
	
	private static String normalize(String string) {
		if (string == null) {
			return null;
		}
		return string.toLowerCase(Locale.ROOT);
	}
	
	public static PopoverTrigger fromString(String value) {
		for (PopoverTrigger type : values()) {
			if (type.getValue().equals(normalize(value))) {
				return type;
			}
		}
		return null;
	}
}