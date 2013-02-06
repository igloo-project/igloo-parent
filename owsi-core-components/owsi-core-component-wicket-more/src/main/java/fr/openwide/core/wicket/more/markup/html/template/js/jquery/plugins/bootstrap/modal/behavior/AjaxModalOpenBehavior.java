package fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.bootstrap.modal.behavior;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.AjaxCallListener;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.ajax.attributes.IAjaxCallListener;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.odlabs.wiquery.core.events.EventLabel;
import org.odlabs.wiquery.core.javascript.JsStatement;

import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.bootstrap.modal.BootstrapModalJavaScriptResourceReference;
import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.bootstrap.modal.component.IAjaxModalPopupPanel;
import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.bootstrap.modal.statement.BootstrapModal;
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

	private final BootstrapModal options;

	/**
	 * @param modalPopupPanel
	 * @param event
	 */
	public AjaxModalOpenBehavior(IAjaxModalPopupPanel modalPopupPanel, EventLabel event) {
		this(modalPopupPanel, event, null);
	}

	/**
	 * @param modalPopupPanel
	 * @param event
	 * @param options - peut être null (options par défaut)
	 */
	public AjaxModalOpenBehavior(IAjaxModalPopupPanel modalPopupPanel, EventLabel event, BootstrapModal options) {
		super(event.getEventLabel());
		this.modal = modalPopupPanel;
		this.options = options;
	}

	@Override
	protected final void onEvent(AjaxRequestTarget target) {
		onShow(target);
		modal.show(target);
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
		onSuccessStatement.append(BootstrapModalManagerStatement.show(modal.getContainer(), options).render(true));
		openModalListener.onSuccess(onSuccessStatement.render());
		
		openModalListener.onFailure(BootstrapModalManagerStatement.removeLoading().render());
		
		return openModalListener;
	}

	@Override
	public void renderHead(Component component, IHeaderResponse response) {
		super.renderHead(component, response);
		response.render(JavaScriptHeaderItem.forReference(BootstrapModalJavaScriptResourceReference.get()));
	}

	/**
	 * Code appelé avant tout traitement de l'événement.
	 */
	public JsStatement onModalStart() {
		return null;
	}

	/**
	 * Code appelé au momoent de l'affichage du popup. Dans la plupart des cas, il est plus judicieux d'utiliser le
	 * {@link AjaxRequestTarget} du {@link AjaxModalOpenBehavior#onShow(AjaxRequestTarget)}. Cette méthode est là par
	 * mimétisme de {@link ModalOpenOnClickBehavior}
	 */
	public JsStatement onModalComplete() {
		return null;
	}

}
