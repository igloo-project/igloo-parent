package fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.fancybox;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.behavior.AbstractAjaxBehavior;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.odlabs.wiquery.core.javascript.JsStatement;

/**
 * Behavior associant un élément de type lien et un composant à afficher dans une
 * fancybox.
 * 
 * L'élément qui apparaît dans la fancybox est copié à partir de l'élément wicket
 * d'origine. Cet élément doit être embarqué dans un div style="display: none;"
 * pour éviter d'apparaître sur la page avant la sollicitation de fancybox.
 * 
 * L'élément est rafraîchi par un appel ajax avant d'être affiché.
 * 
 * parent est l'élément parent, qui doit toujours exister dans la page.
 * 
 * component est un élément enfant de parent, dont l'état visible sera basculé lors
 * de l'appel ajax. Ceci permet d'avoir un composant qui ne sera initialisé qu'au
 * moment de l'appel ajax.
 * 
 * On peut avoir parent = component ; dans ce cas, il ne faut pas faire
 * component.setVisible(false) sinon l'élément n'existe pas dans la page et ne
 * peut pas être initialisé via ajax.
 *
 */
public class AjaxFancyboxHtmlPanelBehavior extends AbstractAjaxBehavior {

	private static final long serialVersionUID = 6414097982857106898L;

	private Component parent;

	public AjaxFancyboxHtmlPanelBehavior(Component parent) {
		super();
		parent.setOutputMarkupId(true);
		this.parent = parent;
	}

	@Override
	public void onBind() {
		super.onBind();
		
		getComponent().add(new AjaxEventBehavior("onclick") {
			
			private static final long serialVersionUID = -1194316821232521566L;

			@Override
			protected void onEvent(AjaxRequestTarget target) {
				onPopupShow();
				AjaxFancyboxHtmlPanelBehavior.this.getComponent().setVisible(true);
				target.add(parent);
				DefaultTipsyFancybox fancybox = new FancyboxAnchor(parent);
				target.appendJavaScript(new JsStatement().$().chain(fancybox).render());
			}
		});
	}

	protected void onPopupShow() {
		// override this to do things on popup show
	}

	@Override
	public void renderHead(Component component, IHeaderResponse response) {
		response.render(JavaScriptHeaderItem.forReference(FancyboxJavaScriptResourceReference.get()));
		response.render(CssHeaderItem.forReference(FancyboxStyleSheetResourceReference.get()));
	}

	@Override
	public void onRequest() {
		// TODO Auto-generated method stub
	}

}
