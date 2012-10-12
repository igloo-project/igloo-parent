package fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.fancybox;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.behavior.AbstractAjaxBehavior;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.model.LoadableDetachableModel;
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
public class FancyboxHtmlPanelBehavior extends AbstractAjaxBehavior {

	private static final long serialVersionUID = 6414097982857106898L;
	
	public FancyboxHtmlPanelBehavior(Component component) {
		super();
	}
	
	@Override
	public void onBind() {
		super.onBind();
		
		getComponent().setOutputMarkupId(true);
		getComponent().add(new AttributeModifier("href", new LoadableDetachableModel<String>() {
			private static final long serialVersionUID = -44670100964325215L;

			@Override
			protected String load() {
				return "#" + FancyboxHtmlPanelBehavior.this.getComponent().getMarkupId();
			}
		}));
	}

	public JsStatement statement() {
		DefaultTipsyFancybox fancybox = new DefaultTipsyFancybox();
		return new JsStatement().$(getComponent()).chain(fancybox);
	}

	@Override
	public void renderHead(Component component, IHeaderResponse response) {
		response.render(JavaScriptHeaderItem.forReference(FancyboxJavaScriptResourceReference.get()));
		response.render(CssHeaderItem.forReference(FancyboxStyleSheetResourceReference.get()));
		
		response.render(OnDomReadyHeaderItem.forScript(statement().render()));
	}

	@Override
	public void onRequest() {
		// TODO Auto-generated method stub
	}
}
