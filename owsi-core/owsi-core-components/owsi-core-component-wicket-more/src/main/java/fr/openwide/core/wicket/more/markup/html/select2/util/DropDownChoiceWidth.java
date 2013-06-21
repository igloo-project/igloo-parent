package fr.openwide.core.wicket.more.markup.html.select2.util;

/**
 * DropDownChoice size, defined in px.<br>
 * Awful hack to work around an issue of Firefox computing incorrect width in some specific cases.
 */
public enum DropDownChoiceWidth {
	
	SMALL(104),
	NORMAL(220),
	DOUBLE(443),
	XLARGE(284),
	XXLARGE(534);
	
	private int width;
	
	private DropDownChoiceWidth(int width) {
		this.width = width;
	}
	
	public int getWidth() {
		return width;
	}

}
