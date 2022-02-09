package org.iglooproject.bootstrap.api;

import org.wicketstuff.wiquery.core.javascript.JsUtils;
import org.wicketstuff.wiquery.core.options.IComplexOption;
import org.wicketstuff.wiquery.core.options.ITypedOption;

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
