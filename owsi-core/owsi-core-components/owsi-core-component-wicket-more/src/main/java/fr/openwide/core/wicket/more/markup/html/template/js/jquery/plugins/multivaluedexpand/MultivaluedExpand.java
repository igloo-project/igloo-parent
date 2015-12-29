package fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.multivaluedexpand;

import java.io.Serializable;

import org.odlabs.wiquery.core.javascript.ChainableStatement;
import org.odlabs.wiquery.core.javascript.JsUtils;
import org.odlabs.wiquery.core.options.Options;

public class MultivaluedExpand implements ChainableStatement, Serializable {
	
	private static final long serialVersionUID = 4804809311019571322L;
	
	private static final String MULTIVALUED_EXPAND = "multivaluedExpand";
	
	private String toggleButtonHtml;
	
	public MultivaluedExpand() {
		super();
	}
	
	@Override
	public String chainLabel() {
		return MULTIVALUED_EXPAND;
	}
	
	@Override
	public CharSequence[] statementArgs() {
		Options options = new Options();
		
		if (toggleButtonHtml != null) {
			options.put("toggleButtonHtml", JsUtils.quotes(toggleButtonHtml, true));
		}
		
		return new CharSequence[] { options.getJavaScriptOptions() };
	}
	
	/**
	 * Allows to override default "toggle" button.<br /><br />
	 * 
	 * Default button: see JS file.<br />
	 * Override sample (glyphicon):<br />
	 * {@code
	 * 	<a><span class='icon-plus-sign' /><span class='icon-minus-sign' /></a>
	 * }
	 * 
	 * @param toggleButtonHtml HTML code that represents the toggle button
	 * @return this
	 */
	public MultivaluedExpand toggleButtonHtml(String toggleButtonHtml) {
		this.toggleButtonHtml = toggleButtonHtml;
		return this;
	}
}
