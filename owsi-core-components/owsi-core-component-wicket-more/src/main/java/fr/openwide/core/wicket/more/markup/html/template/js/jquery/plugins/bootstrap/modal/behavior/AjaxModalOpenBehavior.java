package fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.bootstrap.modal.behavior;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.AjaxCallListener;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.ajax.attributes.IAjaxCallListener;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.odlabs.wiquery.core.events.Event;
import org.odlabs.wiquery.core.events.EventLabel;
import org.odlabs.wiquery.core.javascript.JsScope;
import org.odlabs.wiquery.core.javascript.JsScopeEvent;
import org.odlabs.wiquery.core.javascript.JsStatement;

import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.bootstrap.modal.BootstrapModalJavaScriptResourceReference;
import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.bootstrap.modal.component.IAjaxModalPopupPanel;
import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.bootstrap.modal.statement.BootstrapModalEvent;
import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.bootstrap.modal.statement.BootstrapModalManagerStatement;

/**
 * Permet de provoquer l'ouverture d'un popup via une requête ajax. L'ordre des événements est le suivant :
 * 
 * 1. attente du popup
 * 2. requête ajax
 * 3. arrêt du mode attente et affichage du popup avec la réponse
 */
public abstract class AjaxModalOpenBehavior extends AjaxEventBehavior {

	private static final long serialVersionUID = 3299212684157849227L;

	private final IAjaxModalPopupPanel modal;

	/**
	 * @param modalPopupPanel
	 * @param event
	 */
	public AjaxModalOpenBehavior(IAjaxModalPopupPanel modalPopupPanel, EventLabel event) {
		super(event.getEventLabel());
		this.modal = modalPopupPanel;
	}

	@Override
	protected final void onEvent(AjaxRequestTarget target) {
		onShow(target);
		modal.show(target);
		
		// si on renvoie le container, il faut renvoyer ses événements
		if (target.getComponents().contains(modal.getContainer())) {
			target.appendJavaScript(onModalHideBindStatement().render());
			target.appendJavaScript(onModalShowBindStatement().render());
		}
	}

	protected abstract void onShow(AjaxRequestTarget target);

	@Override
	protected void updateAjaxAttributes(AjaxRequestAttributes attributes) {
		attributes.getAjaxCallListeners().add(getOpenModalCallListener());
		
		super.updateAjaxAttributes(attributes);
	}

	protected IAjaxCallListener getOpenModalCallListener() {
		AjaxCallListener openModalListener = new AjaxCallListener();
		
		// avant d'effectuer la requête ajax : onModalStart + écran d'attente
		JsStatement onBeforeStatement = new JsStatement();
		JsStatement onModalStart = onModalStart();
		if (onModalStart != null) {
			onBeforeStatement.append(onModalStart.render(true));
		}
		onBeforeStatement.append(BootstrapModalManagerStatement.loading().render(true));
		openModalListener.onBefore(onBeforeStatement.render());
		
		// après retour ajax, on exécute le onModalComplete puis on affiche la popup
		JsStatement onSuccessStatement = new JsStatement();
		JsStatement onModalComplete = onModalComplete();
		if (onModalComplete != null) {
			onSuccessStatement.append(onModalComplete.render(true));
		}
		onSuccessStatement.append(BootstrapModalManagerStatement.show(modal.getContainer(), modal.getBootstrapModal()).render(true));
		openModalListener.onSuccess(onSuccessStatement.render());
		
		openModalListener.onFailure(BootstrapModalManagerStatement.removeLoading().render());
		
		return openModalListener;
	}
	
	/**
	 * Rend le composant attaché invisible si la popup est invisible
	 */
	@Override
	public void onConfigure(Component component) {
		super.onConfigure(component);
		component.setVisibilityAllowed(modal.determineVisibility());
	}

	@Override
	public void renderHead(Component component, IHeaderResponse response) {
		super.renderHead(component, response);
		response.render(JavaScriptHeaderItem.forReference(BootstrapModalJavaScriptResourceReference.get()));
		
		// enregistrement des événements onShow et onHide
		response.render(OnDomReadyHeaderItem.forScript(onModalShowBindStatement().render()));
		response.render(OnDomReadyHeaderItem.forScript(onModalHideBindStatement().render()));
	}

	protected final JsStatement onModalShowBindStatement() {
		Event onShow = new Event(BootstrapModalEvent.SHOW) {
			private static final long serialVersionUID = -5947286377954553132L;
			
			@Override
			public JsScope callback() {
				return JsScopeEvent.quickScope(onModalShow());
			}
		};
		return new JsStatement().$(modal.getContainer()).chain(onShow);
	}

	protected final JsStatement onModalHideBindStatement() {
		Event onHide = new Event(BootstrapModalEvent.HIDE) {
			private static final long serialVersionUID = -5947286377954553132L;
			
			@Override
			public JsScope callback() {
				return JsScopeEvent.quickScope(onModalHide());
			}
		};
		return new JsStatement().$(modal.getContainer()).chain(onHide);
	}

	/**
	 * Code appelé avant tout traitement de l'événement.
	 */
	protected JsStatement onModalStart() {
		return null;
	}

	/**
	 * Code appelé au momoent de l'affichage du popup. Dans la plupart des cas, il est plus judicieux d'utiliser le
	 * {@link AjaxRequestTarget} du {@link AjaxModalOpenBehavior#onShow(AjaxRequestTarget)}. Cette méthode est là par
	 * mimétisme de {@link ModalOpenOnClickBehavior}
	 */
	protected JsStatement onModalComplete() {
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

}
