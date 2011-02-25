package fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.fancybox;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.odlabs.wiquery.core.behavior.WiQueryAbstractBehavior;
import org.odlabs.wiquery.core.commons.WiQueryResourceManager;
import org.odlabs.wiquery.core.javascript.JsStatement;
import org.odlabs.wiquery.core.javascript.JsUtils;

/**
 * Behavior associant un élément de type lien et un composant {@link FancyboxPopupPanel}
 */
public class FancyboxPopupPanelBehavior extends WiQueryAbstractBehavior {

	private static final long serialVersionUID = 6414097982857106898L;
	
	private FancyboxPopupPanel fancyboxPopupPanel;
	
	public FancyboxPopupPanelBehavior(FancyboxPopupPanel fancyboxPopupPanel) {
		super();
		this.fancyboxPopupPanel = fancyboxPopupPanel;
	}
	
	@Override
	public void bind(Component link) {
		super.bind(link);
		
		link.add(new AjaxEventBehavior("onclick") {
			
			private static final long serialVersionUID = -1194316821232521566L;

			@Override
			protected void onEvent(AjaxRequestTarget target) {
				onPopupShow();
				fancyboxPopupPanel.show(target);
				DefaultTipsyFancybox fancybox = new FancyboxAnchor(fancyboxPopupPanel.getReplaceableContainer());
				target.appendJavascript(new JsStatement().$().chain(fancybox).render().toString());
				String markupId = "#" + fancyboxPopupPanel.getReplaceableContainer().getMarkupId();
				target.appendJavascript(new JsStatement().append("document.lastPopupElement")
						.append(" = ")
						.append(JsUtils.quotes(markupId)).render().toString());
			}
		});
	}
	
	protected void onPopupShow() {
		// override this to do things on popup show
	}

	@Override
	public JsStatement statement() {
		return null;
	}
	
	@Override
	public void contribute(WiQueryResourceManager wiQueryResourceManager) {
		super.contribute(wiQueryResourceManager);
		wiQueryResourceManager.addJavaScriptResource(FancyboxJavaScriptResourceReference.get());
		wiQueryResourceManager.addCssResource(FancyboxStyleSheetResourceReference.get());
	}

}
