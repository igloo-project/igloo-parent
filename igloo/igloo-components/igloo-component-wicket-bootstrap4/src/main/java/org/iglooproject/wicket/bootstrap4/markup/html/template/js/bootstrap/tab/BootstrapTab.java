package org.iglooproject.wicket.bootstrap4.markup.html.template.js.bootstrap.tab;

import java.io.Serializable;

import org.wicketstuff.wiquery.core.javascript.ChainableStatement;

public class BootstrapTab implements ChainableStatement, Serializable {

	private static final long serialVersionUID = -5404079169397799951L;

	@Override
	public String chainLabel() {
		return "tab";
	}

	@Override
	public CharSequence[] statementArgs() {
		return new CharSequence[0];
	}

}