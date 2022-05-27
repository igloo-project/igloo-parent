package org.iglooproject.wicket.modal;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.AjaxCallListener;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.ajax.attributes.IAjaxCallListener;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.iglooproject.bootstrap.api.BootstrapRequestCycle;
import org.iglooproject.bootstrap.api.IAjaxModalPopupPanel;
import org.wicketstuff.wiquery.core.events.EventLabel;
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
		
		JsStatement onSuccessStatement = new JsStatement();
		onSuccessStatement.append(modal.getBootstrapModal().show(modal.getContainer()).render(true));
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
		BootstrapRequestCycle.getSettings().modalRenderHead(component, response);
	}


}
