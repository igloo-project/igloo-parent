package fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.tipsy;

public enum TipsyOptionGravity {

	NORTH("'n'"),
	SOUTH("'s'"),
	EAST("'e'"),
	WEST("'w'"),
	NORTH_WEST("'nw'"),
	NORTH_EAST("'ne'"),
	SOUTH_WEST("'sw'"),
	SOUTH_EAST("'se'");	
	
	private String value;
	
	private TipsyOptionGravity(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}
}
