package fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.tipsy;

public enum TipsyOptionTrigger {

	HOVER("'hover'"),
	FOCUS("'focus'"),
	MANUAL("'manual'");
	
	private String value;
	
	private TipsyOptionTrigger(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}
}
