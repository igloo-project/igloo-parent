package org.iglooproject.wicket.bootstrap4.markup.html.template.js.bootstrap.modal.behavior;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.AjaxCallListener;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.ajax.attributes.IAjaxCallListener;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.iglooproject.bootstrap.api.BootstrapModalUtils;
import org.iglooproject.bootstrap.api.IAjaxModalPopupPanel;
import org.iglooproject.wicket.bootstrap4.markup.html.template.js.bootstrap.modal.BootstrapModalJavaScriptResourceReference;
import org.iglooproject.wicket.bootstrap4.markup.html.template.js.bootstrap.modal.statement.BootstrapModalEvent;
import org.wicketstuff.wiquery.core.events.Event;
import org.wicketstuff.wiquery.core.events.EventLabel;
import org.wicketstuff.wiquery.core.javascript.JsScope;
import org.wicketstuff.wiquery.core.javascript.JsScopeEvent;
import org.wicketstuff.wiquery.core.javascript.JsStatement;

public class AjaxModalOpenBehavior extends AjaxEventBehavior {

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

	/**
	 * Exécuté juste avant l'appel de modal.show()
	 */
	protected void onShow(AjaxRequestTarget target) {
		// Ne fait rien par défaut.
	}

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
		openModalListener.onBefore(onBeforeStatement.render());
		
		// après retour ajax, on exécute le onModalComplete puis on affiche la popup
		JsStatement onSuccessStatement = new JsStatement();
		JsStatement onModalComplete = onModalComplete();
		if (onModalComplete != null) {
			onSuccessStatement.append(onModalComplete.render(true));
		}
		onSuccessStatement.append(BootstrapModalUtils.show(modal.getContainer(), modal.getBootstrapModal()).render(true));
		openModalListener.onSuccess(onSuccessStatement.render());
		
		
		return openModalListener;
	}
	
	/**
	 * Rend le composant attaché invisible si la popup est invisible
	 */
	@Override
	public void onConfigure(Component component) {
		super.onConfigure(component);
		modal.configure();
		
		setUpAttachedComponent(component, modal.determineVisibility());
	}
	
	protected void setUpAttachedComponent(Component component, boolean modalIsVisible) {
		component.setVisibilityAllowed(modalIsVisible);
	}

	@Override
	public void renderHead(Component component, IHeaderResponse response) {
		super.renderHead(component, response);
		
		response.render(JavaScriptHeaderItem.forReference(BootstrapModalJavaScriptResourceReference.get()));
		
		// enregistrement des événements onShow et onHide
		response.render(OnDomReadyHeaderItem.forScript(onModalShowBindStatement().render()));
		response.render(OnDomReadyHeaderItem.forScript(onModalHideBindStatement().render()));
	}

	/**
	 * @deprecated Should no longer be used because the JavaScript code is executed as many times as there are buttons
	 * bound to the modal. Use {@link ModalOpenOnClickBehavior#onModalShow()} instead.
	 */
	@Deprecated
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

	/**
	 * @deprecated Should no longer be used because the JavaScript code is executed as many times as there are buttons
	 * bound to the modal. Use {@link ModalOpenOnClickBehavior#onModalHide()} instead.
	 */
	@Deprecated
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
	 * @deprecated Should no longer be used because the JavaScript code is executed as many times as there are buttons
	 * bound to the modal. Use {@link ModalOpenOnClickBehavior#onModalStart()} instead.
	 */
	@Deprecated
	protected JsStatement onModalStart() {
		return null;
	}

	/**
	 * @deprecated Should no longer be used because the JavaScript code is executed as many times as there are buttons
	 * bound to the modal. Use {@link ModalOpenOnClickBehavior#onModalComplete()} instead.
	 */
	@Deprecated
	protected JsStatement onModalComplete() {
		return null;
	}

	/**
	 * @deprecated Should no longer be used because the JavaScript code is executed as many times as there are buttons
	 * bound to the modal. Use {@link ModalOpenOnClickBehavior#onModalShow()} instead.
	 */
	@Deprecated
	public JsStatement onModalShow() {
		return null;
	}

	/**
	 * @deprecated Should no longer be used because the JavaScript code is executed as many times as there are buttons
	 * bound to the modal. Use {@link ModalOpenOnClickBehavior#onModalHide()} instead.
	 */
	@Deprecated
	public JsStatement onModalHide() {
		return null;
	}

}
