package fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.more;

public class More implements IMore {

	private static final long serialVersionUID = -7954609723734757695L;
	public static final String ATTRIBUTE_LABEL = "data-more-label";

	@Override
	public String chainLabel() {
		return "more";
	}

	@Override
	public CharSequence[] statementArgs() {
		return new CharSequence[] {};
	}

}
