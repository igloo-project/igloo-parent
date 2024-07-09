package basicapp.front.common.select2.util;

import org.iglooproject.wicket.more.markup.html.select2.util.IDropDownChoiceWidth;

public enum DropDownChoiceWidth implements IDropDownChoiceWidth {

	NORMAL("300px");

	private final String width;

	private DropDownChoiceWidth(String width) {
		this.width = width;
	}

	@Override
	public String getWidth() {
		return width;
	}

}
