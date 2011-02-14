package fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.fancybox;

import java.io.Serializable;

import org.odlabs.wiquery.core.javascript.ChainableStatement;
import org.odlabs.wiquery.core.javascript.JsScope;
import org.odlabs.wiquery.core.options.Options;

import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.tipsy.TipsyHelper;

/**
 * Gestion des options de Fancybox.
 * 
 * On ajoute d'office le fait de fermer l'ensemble des éléments Tipsy de la page.
 */
public class DefaultTipsyFancybox implements ChainableStatement, Serializable {
	private static final long serialVersionUID = 7388843679060298459L;
	
	private Options options = new Options();
	
	private FancyBoxOptionType type;
	
	public DefaultTipsyFancybox() {
		JsScope scope = JsScope.quickScope(TipsyHelper.closeTipsyStatement().render());
		options.put("onStart", scope);
	}
	
	public void setType(FancyBoxOptionType type) {
		this.type = type;
	}

	@Override
	public String chainLabel() {
		return "fancybox";
	}

	@Override
	public CharSequence[] statementArgs() {
		if (type != null) {
			options.put("type", type.getValue());
		}
		
		return new CharSequence[] { options.getJavaScriptOptions() };
	}

}
