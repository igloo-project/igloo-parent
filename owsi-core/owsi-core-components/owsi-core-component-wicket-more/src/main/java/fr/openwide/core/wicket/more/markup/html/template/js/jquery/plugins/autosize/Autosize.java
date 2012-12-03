package fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.autosize;

import java.io.Serializable;

import org.odlabs.wiquery.core.javascript.ChainableStatement;
import org.odlabs.wiquery.core.javascript.JsUtils;
import org.odlabs.wiquery.core.options.Options;

public class Autosize implements ChainableStatement, Serializable {

	private static final long serialVersionUID = 4530534546458613607L;
	
	private String append;
	
	@Override
	public String chainLabel() {
		return "autosize";
	}

	@Override
	public CharSequence[] statementArgs() {
		Options options = new Options();
		if (append != null) {
			options.put("append", JsUtils.quotes(append));
		}
		
		CharSequence[] args = new CharSequence[1];
		args[0] = options.getJavaScriptOptions();
		return args;
	}
	
	public Autosize append(String append) {
		this.append = append;
		return this;
	}

}
