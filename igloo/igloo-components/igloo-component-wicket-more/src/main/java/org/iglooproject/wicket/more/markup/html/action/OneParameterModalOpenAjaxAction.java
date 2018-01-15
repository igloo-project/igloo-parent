package org.iglooproject.wicket.more.markup.html.action;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.AjaxCallListener;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.ajax.attributes.IAjaxCallListener;
import org.iglooproject.wicket.more.condition.Condition;
import org.iglooproject.wicket.more.markup.html.template.js.bootstrap.modal.component.AbstractAjaxModalPopupPanel;
import org.iglooproject.wicket.more.markup.html.template.js.bootstrap.modal.statement.BootstrapModalManagerStatement;

public class OneParameterModalOpenAjaxAction<T> extends AbstractOneParameterAjaxAction<T> {

	private static final long serialVersionUID = 511391011732361800L;

	private final AbstractAjaxModalPopupPanel<?> modal;

	public OneParameterModalOpenAjaxAction(AbstractAjaxModalPopupPanel<?> modal) {
		super();
		this.modal = modal;
	}

	@Override
	public void execute(AjaxRequestTarget target, T parameter) {
		onShow(target, parameter);
		modal.show(target);
	}

	@Override
	public void updateAjaxAttributes(AjaxRequestAttributes attributes, T parameter) {
		attributes.getAjaxCallListeners().add(getOpenModalCallListener());
		super.updateAjaxAttributes(attributes, parameter);
	}

	protected IAjaxCallListener getOpenModalCallListener() {
		AjaxCallListener openModalListener = new AjaxCallListener();
		openModalListener.onBefore(BootstrapModalManagerStatement.loading().render(true));
		openModalListener.onSuccess(BootstrapModalManagerStatement.show(modal.getContainer(), modal.getBootstrapModal()).render(true));
		openModalListener.onFailure(BootstrapModalManagerStatement.removeLoading().render());
		return openModalListener;
	}

	protected void onShow(AjaxRequestTarget target, T parameter) {
		// Ne fait rien par défaut.
	}

	@Override
	public Condition getActionAvailableCondition(T parameter) {
		return Condition.visible(modal);
	}

	public AbstractAjaxModalPopupPanel<?> getModal() {
		return modal;
	}

}
