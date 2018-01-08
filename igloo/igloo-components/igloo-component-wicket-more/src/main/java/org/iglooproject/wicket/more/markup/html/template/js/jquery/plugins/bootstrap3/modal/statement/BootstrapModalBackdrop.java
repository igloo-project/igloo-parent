package org.iglooproject.wicket.more.markup.html.template.js.jquery.plugins.bootstrap3.modal.statement;

import org.wicketstuff.wiquery.core.javascript.JsUtils;
import org.wicketstuff.wiquery.core.options.IComplexOption;
import org.wicketstuff.wiquery.core.options.ITypedOption;

/**
 * @deprecated Use Bootstrap 4 CSS et JS from now on.
 */
@Deprecated
public enum BootstrapModalBackdrop implements ITypedOption<BootstrapModalBackdrop>, IComplexOption {

	NORMAL(Boolean.TRUE.toString()),
	STATIC(JsUtils.quotes("static"));

	private final String javascriptValue;

	private BootstrapModalBackdrop(String javascriptValue) {
		this.javascriptValue = javascriptValue;
	}

	@Override
	public CharSequence getJavascriptOption() {
		return javascriptValue;
	}

	@Override
	public BootstrapModalBackdrop getValue() {
		return this;
	}

	

}
