package fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.caroufredsel;

public enum CarouFredSelDirection {

	UP("up"),
	DOWN("down"),
	LEFT("left"),
	RIGHT("right")
	;
	
	private final String value;
	
	private CarouFredSelDirection(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}
}
