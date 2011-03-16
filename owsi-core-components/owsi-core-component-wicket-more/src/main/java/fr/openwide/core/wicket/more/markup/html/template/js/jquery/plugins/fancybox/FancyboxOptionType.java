package fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.fancybox;

public enum FancyboxOptionType {

	HTML("'html'"),
	IMAGE("'image'"),
	SWF("'swf'"),
	IFRAME("'iframe'"),
	INLINE("'inline'"),
	AJAX("'ajax'");
	
	private String value;
	
	private FancyboxOptionType(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}
}
