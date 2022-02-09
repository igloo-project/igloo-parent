package org.iglooproject.wicket.bootstrap5.markup.html.template.js.bootstrap.modal.behavior;

import org.apache.wicket.Component;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.iglooproject.bootstrap.api.IModalPopupPanel;
import org.iglooproject.wicket.bootstrap5.markup.html.template.js.bootstrap.modal.statement.BootstrapModalEvent;
import org.iglooproject.wicket.bootstrap5.markup.html.template.js.bootstrap.modal.statement.BootstrapModalStatement;
import org.iglooproject.wicket.more.markup.html.template.js.jquery.plugins.util.JQueryAbstractBehavior;
import org.wicketstuff.wiquery.core.events.Event;
import org.wicketstuff.wiquery.core.events.MouseEvent;
import org.wicketstuff.wiquery.core.javascript.JsScope;
import org.wicketstuff.wiquery.core.javascript.JsScopeEvent;
import org.wicketstuff.wiquery.core.javascript.JsStatement;

public class ModalOpenOnClickBehavior extends JQueryAbstractBehavior {

	private static final long serialVersionUID = 8188257386595829052L;

	private final IModalPopupPanel modal;

	/**
	 * @param modal - le composant qui contient la popup
	 */
	public ModalOpenOnClickBehavior(IModalPopupPanel modal) {
		super();
		this.modal = modal;
	}

	protected JsStatement getBindClickStatement() {
		if (!getComponent().isEnabledInHierarchy()) {
			return null;
		}
		
		Event event = new Event(MouseEvent.CLICK) {
			private static final long serialVersionUID = 1410592312776274815L;
			
			@Override
			public JsScope callback() {
				JsStatement jsStatement = new JsStatement();
				JsStatement onModalStart = onModalStart();
				JsStatement onModalComplete = onModalComplete();
				if (onModalStart != null) {
					jsStatement.append(onModalStart.render(true));
				}
				jsStatement.append(BootstrapModalStatement.show(modal.getContainer(), modal.getBootstrapModal()).render(true));
				if (onModalComplete != null) {
					jsStatement.append(onModalComplete.render(true));
				}
				return JsScope.quickScope(jsStatement);
			}
		};
		return new JsStatement().$(getComponent()).chain(event);
	}

	/**
	 * Code appelé avant tout traitement de l'événement d'affichage.
	 */
	public JsStatement onModalStart() {
		return null;
	}

	/**
	 * Code appelé au moment avant de demander l'affichage de la popup.
	 */
	public JsStatement onModalComplete() {
		return null;
	}

	/**
	 * Code appelé au moment de l'affichage du popup.
	 */
	public JsStatement onModalShow() {
		return null;
	}

	/**
	 * Code appelé quand le popup est caché.
	 */
	public JsStatement onModalHide() {
		return null;
	}
	
	/**
	 * Rend le composant attaché invisible si la popup est invisible
	 */
	@Override
	public void onConfigure(Component component) {
		super.onConfigure(component);
		modal.configure();
		component.setVisibilityAllowed(modal.determineVisibility());
	}

	@Override
	public void renderHead(Component component, IHeaderResponse response) {
		super.renderHead(component, response);
		
		JsStatement bindClickStatement = getBindClickStatement();
		if (bindClickStatement != null) {
			response.render(OnDomReadyHeaderItem.forScript(getBindClickStatement().render()));
			
			Event onShow = new Event(BootstrapModalEvent.SHOW) {
				private static final long serialVersionUID = -5947286377954553132L;
				
				@Override
				public JsScope callback() {
					return JsScopeEvent.quickScope(onModalShow());
				}
			};
			Event onHide = new Event(BootstrapModalEvent.HIDE) {
				private static final long serialVersionUID = -5947286377954553132L;
				
				@Override
				public JsScope callback() {
					return JsScopeEvent.quickScope(onModalHide());
				}
			};
			
			// enregistrement des événements onShow et onHide
			response.render(OnDomReadyHeaderItem.forScript(new JsStatement().$(modal.getContainer()).chain(onShow).render()));
			response.render(OnDomReadyHeaderItem.forScript(new JsStatement().$(modal.getContainer()).chain(onHide).render()));
		}
	}

}
