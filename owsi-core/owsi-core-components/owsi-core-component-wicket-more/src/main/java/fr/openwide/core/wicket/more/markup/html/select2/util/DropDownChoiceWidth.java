package fr.openwide.core.wicket.more.markup.html.select2.util;

public enum DropDownChoiceWidth implements IDropDownChoiceWidth {
	
	SMALL(104),
	NORMAL(220),
	XLARGE(284),
	XXLARGE(534);
	
	private int width;
	
	private DropDownChoiceWidth(int width) {
		this.width = width;
	}
	
	@Override
	public int getWidth() {
		return width;
	}

}
