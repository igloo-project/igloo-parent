package fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.modal;

import org.odlabs.wiquery.core.options.Options;

public final class Modal implements IModal {

	private static final long serialVersionUID = -6278874757976442373L;

	@Override
	public String chainLabel() {
		return "fancybox";
	}

	@Override
	public CharSequence[] statementArgs() {
		Options options = new Options();
		return new CharSequence[] { options.getJavaScriptOptions() };
	}

}
