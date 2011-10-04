package fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.fancybox;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.model.LoadableDetachableModel;
import org.odlabs.wiquery.core.behavior.WiQueryAbstractBehavior;
import org.odlabs.wiquery.core.commons.WiQueryResourceManager;
import org.odlabs.wiquery.core.javascript.JsStatement;

/**
 * Behavior associant un élément de type lien et un composant à afficher dans une
 * fancybox.
 * 
 * L'élément qui apparaît dans la fancybox est copié à partir de l'élément wicket
 * d'origine. Cet élément doit être embarqué dans un div style="display: none;"
 * pour éviter d'apparaître sur la page avant la sollicitation de fancybox.
 *
 */
public class FancyboxHtmlPanelBehavior extends WiQueryAbstractBehavior {

	private static final long serialVersionUID = 6414097982857106898L;
	
	private Component component;
	
	public FancyboxHtmlPanelBehavior(Component component) {
		super();
		this.component = component;
		this.component.setOutputMarkupId(true);
	}
	
	@Override
	public void bind(Component component) {
		super.bind(component);
		component.setOutputMarkupId(true);
		component.add(new AttributeModifier("href", true, new LoadableDetachableModel<String>() {
			private static final long serialVersionUID = -44670100964325215L;

			@Override
			protected String load() {
				return "#" + FancyboxHtmlPanelBehavior.this.component.getMarkupId();
			}
		}));
	}

	@Override
	public JsStatement statement() {
		DefaultTipsyFancybox fancybox = new DefaultTipsyFancybox();
		return new JsStatement().$(getComponent()).chain(fancybox);
	}
	
	@Override
	public void contribute(WiQueryResourceManager wiQueryResourceManager) {
		super.contribute(wiQueryResourceManager);
		wiQueryResourceManager.addJavaScriptResource(FancyboxJavaScriptResourceReference.get());
		wiQueryResourceManager.addCssResource(FancyboxStyleSheetResourceReference.get());
	}

}
