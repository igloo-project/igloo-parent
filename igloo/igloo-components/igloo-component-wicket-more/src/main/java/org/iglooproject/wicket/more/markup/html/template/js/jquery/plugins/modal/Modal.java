package org.iglooproject.wicket.more.markup.html.template.js.jquery.plugins.modal;

import org.wicketstuff.wiquery.core.options.Options;

/**
 * @see ModalJavaScriptResourceReference
 */
@Deprecated
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
