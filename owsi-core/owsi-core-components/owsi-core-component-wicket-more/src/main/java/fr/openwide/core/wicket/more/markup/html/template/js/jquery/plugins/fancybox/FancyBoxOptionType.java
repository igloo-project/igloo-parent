package fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.fancybox;

public enum FancyBoxOptionType {

	HTML("'html'"),
	IMAGE("'image'"),
	SWF("'swf'"),
	IFRAME("'iframe'"),
	INLINE("'inline'"),
	AJAX("'ajax'");
	
	private String value;
	
	private FancyBoxOptionType(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}
}
