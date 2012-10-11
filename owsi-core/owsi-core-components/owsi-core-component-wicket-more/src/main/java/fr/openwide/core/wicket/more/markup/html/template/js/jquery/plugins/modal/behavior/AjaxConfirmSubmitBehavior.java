package fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.modal.behavior;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.ajax.attributes.IAjaxCallListener;
import org.apache.wicket.ajax.form.AjaxFormSubmitBehavior;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.form.Form;

import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.modal.ModalJavaScriptResourceReference;
import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.modal.misc.ConfirmAjaxCallListener;

public abstract class AjaxConfirmSubmitBehavior extends AjaxFormSubmitBehavior {

	private static final long serialVersionUID = 6052508331550413563L;

	private static final IAjaxCallListener CONFIRM_LISTENER = new ConfirmAjaxCallListener();

	public AjaxConfirmSubmitBehavior(Form<?> form, String event) {
		super(form, event);
	}

	@Override
	protected void updateAjaxAttributes(AjaxRequestAttributes attributes) {
		super.updateAjaxAttributes(attributes);
		attributes.getAjaxCallListeners().add(CONFIRM_LISTENER);
	}

	@Override
	public void renderHead(Component component, IHeaderResponse response) {
		super.renderHead(component, response);
		response.render(JavaScriptHeaderItem.forReference(ModalJavaScriptResourceReference.get()));
	}

}
