package igloo.bootstrap.modal;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.AjaxCallListener;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.ajax.attributes.IAjaxCallListener;

import igloo.bootstrap.BootstrapRequestCycle;
import igloo.wicket.action.IOneParameterAjaxAction;
import igloo.wicket.condition.Condition;

public class OneParameterModalOpenAjaxAction<T> implements IOneParameterAjaxAction<T> {

	private static final long serialVersionUID = 511391011732361800L;

	private final IAjaxModalPopupPanel modal;

	public OneParameterModalOpenAjaxAction(IAjaxModalPopupPanel modal) {
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
		IOneParameterAjaxAction.super.updateAjaxAttributes(attributes, parameter);
	}

	protected IAjaxCallListener getOpenModalCallListener() {
		AjaxCallListener openModalListener = new AjaxCallListener();
		openModalListener.onSuccess(modal.getBootstrapModal().show(modal.getContainer()).render(true));
		return openModalListener;
	}

	protected void onShow(AjaxRequestTarget target, T parameter) {
		// Ne fait rien par défaut.
	}

	@Override
	public Condition getActionAvailableCondition(T parameter) {
		return Condition.visible(BootstrapRequestCycle.getSettings().asComponent(modal));
	}

	public IAjaxModalPopupPanel getModal() {
		return modal;
	}

}
