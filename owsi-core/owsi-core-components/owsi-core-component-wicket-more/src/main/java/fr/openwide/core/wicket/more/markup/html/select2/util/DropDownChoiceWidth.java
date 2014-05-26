package fr.openwide.core.wicket.more.markup.html.select2.util;

public enum DropDownChoiceWidth implements IDropDownChoiceWidth {
	
	AUTO("100%"),
	SMALL("104px"),
	NORMAL("220px"),
	XLARGE("284px"),
	XXLARGE("534px");
	
	private String width;
	
	private DropDownChoiceWidth(String width) {
		this.width = width;
	}
	
	@Override
	public String getWidth() {
		return width;
	}

}
