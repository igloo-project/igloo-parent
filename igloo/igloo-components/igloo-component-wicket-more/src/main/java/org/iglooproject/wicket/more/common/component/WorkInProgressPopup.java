package org.iglooproject.wicket.more.common.component;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.attributes.AjaxCallListener;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.iglooproject.wicket.markup.html.basic.CoreLabel;
import org.iglooproject.wicket.markup.html.panel.InvisiblePanel;
import org.iglooproject.wicket.more.markup.html.template.js.bootstrap.modal.component.AbstractModalPopupPanel;
import org.iglooproject.wicket.more.markup.html.template.js.bootstrap.modal.component.DelegatedMarkupPanel;
import org.iglooproject.wicket.more.markup.html.template.js.bootstrap.modal.statement.BootstrapModal;
import org.iglooproject.wicket.more.markup.html.template.js.bootstrap.modal.statement.BootstrapModalBackdrop;
import org.iglooproject.wicket.more.markup.html.template.js.bootstrap.modal.statement.BootstrapModalStatement;
import org.wicketstuff.wiquery.core.javascript.JsStatement;

public class WorkInProgressPopup extends AbstractModalPopupPanel<String> {
	
	private static final long serialVersionUID = 187656092203183255L;

	public WorkInProgressPopup(String id, IModel<String> messageModel) {
		super(id, messageModel);
		BootstrapModal options = BootstrapModal.modal();
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
		CharSequence hideIfNotRedirecting = doIfNotRedirecting(BootstrapModalStatement.hide(getContainer()));
		AjaxCallListener listener = new AjaxCallListener()
				.onBeforeSend(BootstrapModalStatement.show(getContainer(), getBootstrapModal()).render())
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
}
