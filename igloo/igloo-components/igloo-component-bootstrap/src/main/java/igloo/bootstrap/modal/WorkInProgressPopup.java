package igloo.bootstrap.modal;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.attributes.AjaxCallListener;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.wicketstuff.wiquery.core.javascript.JsStatement;

import igloo.bootstrap.BootstrapRequestCycle;
import igloo.wicket.component.CoreLabel;
import igloo.wicket.markup.html.panel.DelegatedMarkupPanel;
import igloo.wicket.markup.html.panel.InvisiblePanel;

public class WorkInProgressPopup extends AbstractModalPopupPanel<String> {
	
	private static final long serialVersionUID = 187656092203183255L;

	public WorkInProgressPopup(String id, IModel<String> messageModel) {
		super(id, messageModel);
		IBootstrapModal options = BootstrapRequestCycle.getSettings().modal();
		options.setKeyboard(false);
		options.setBackdrop(BootstrapModalBackdrop.STATIC);
		setBootstrapModal(options);
	}
	
	private static CharSequence doIfNotRedirecting(JsStatement statement) {
		return new StringBuilder()
				.append("if (typeof(jqXHR) !== 'undefined' && jqXHR !== null) {")
					.append(new JsStatement().append("var redirectUrl = jqXHR.getResponseHeader('Ajax-Location')").render())
					.append("if (!(typeof(redirectUrl) !== 'undefined' && redirectUrl !== null && redirectUrl !== '')) {")
						.append(statement.render())
					.append("}")
				.append("} else {")
					.append(statement.render())
				.append("}");
	}
	
	public void updateAjaxAttributes(AjaxRequestAttributes attributes) {
		CharSequence hideIfNotRedirecting = doIfNotRedirecting(getBootstrapModal().hide(getContainer()));
		AjaxCallListener listener = new AjaxCallListener()
				.onBeforeSend(getBootstrapModal().modal(getContainer()).render())
				.onBeforeSend(getBootstrapModal().show(getContainer()).render())
				.onComplete(hideIfNotRedirecting);
		attributes.getAjaxCallListeners().add(listener);
	}
	
	@Override
	protected Component createHeader(String wicketId) {
		return new CoreLabel(wicketId, getModel());
	}
	
	@Override
	protected Component createBody(String wicketId) {
		return new DelegatedMarkupPanel(wicketId, WorkInProgressPopup.class);
	}
	
	@Override
	protected final Component createFooter(String wicketId) {
		return new InvisiblePanel(wicketId);
	}
	
	@Override
	public IModel<String> getModalCssClassModel() {
		return Model.of("modal-work-in-progress");
	}
	
	@Override
	public IModel<String> getModalDialogCssClassModel() {
		return Model.of("modal-sm");
	}
}
