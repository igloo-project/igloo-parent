package fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.modal.behavior;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.IAjaxCallDecorator;
import org.apache.wicket.ajax.form.AjaxFormSubmitBehavior;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.form.Form;

import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.modal.ModalJavaScriptResourceReference;
import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.modal.misc.ConfirmAjaxCallDecorator;

public abstract class AjaxConfirmSubmitBehavior extends AjaxFormSubmitBehavior {

	private static final long serialVersionUID = 6052508331550413563L;

	private static final IAjaxCallDecorator CONFIRM_DECORATOR = new ConfirmAjaxCallDecorator();

	public AjaxConfirmSubmitBehavior(Form<?> form, String event) {
		super(form, event);
	}

	@Override
	public void renderHead(Component component, IHeaderResponse response) {
		super.renderHead(component, response);
		response.renderJavaScriptReference(ModalJavaScriptResourceReference.get());
	}

	@Override
	protected IAjaxCallDecorator getAjaxCallDecorator() {
		return CONFIRM_DECORATOR;
	}

}
