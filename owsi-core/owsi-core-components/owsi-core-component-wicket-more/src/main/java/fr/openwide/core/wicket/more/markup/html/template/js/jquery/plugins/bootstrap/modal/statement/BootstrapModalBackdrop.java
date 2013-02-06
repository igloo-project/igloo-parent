package fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.bootstrap.modal.statement;

import org.odlabs.wiquery.core.javascript.JsUtils;
import org.odlabs.wiquery.core.options.IComplexOption;
import org.odlabs.wiquery.core.options.ITypedOption;

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
