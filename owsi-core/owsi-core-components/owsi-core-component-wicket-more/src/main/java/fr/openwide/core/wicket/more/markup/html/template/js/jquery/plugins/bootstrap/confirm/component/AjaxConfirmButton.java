package fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.bootstrap.confirm.component;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormSubmitBehavior;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;
import org.odlabs.wiquery.core.events.Event;
import org.odlabs.wiquery.core.events.MouseEvent;
import org.odlabs.wiquery.core.events.WiQueryEventBehavior;
import org.odlabs.wiquery.core.javascript.JsScope;
import org.odlabs.wiquery.core.javascript.JsScopeEvent;

import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.bootstrap.confirm.behavior.ConfirmContentBehavior;
import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.bootstrap.confirm.statement.BootstrapConfirmStatement;
import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.bootstrap.modal.BootstrapModalJavaScriptResourceReference;

public abstract class AjaxConfirmButton extends Button {

	private static final long serialVersionUID = -132330109149500197L;

	private final Form<?> form;

	public AjaxConfirmButton(String id, IModel<String> titleModel, IModel<String> textModel,
			IModel<String> yesLabelModel, IModel<String> noLabelModel) {
		this(id, titleModel, textModel, yesLabelModel, noLabelModel, null);
	}

	public AjaxConfirmButton(String id, IModel<String> titleModel, IModel<String> textModel,
			IModel<String> yesLabelModel, IModel<String> noLabelModel,
			Form<?> form) {
		super(id, null);
		this.form = form;
		add(new ConfirmContentBehavior(titleModel, textModel, yesLabelModel, noLabelModel));
		
		// Lors du clic, on ouvre la popup de confirmation. Si l'action est confirmée, 
		// on délenche un évènement 'confirm'.
		add(new WiQueryEventBehavior(new Event(MouseEvent.CLICK) {
			private static final long serialVersionUID = 1L;
			@Override
			public JsScope callback() {
				return JsScopeEvent.quickScope(BootstrapConfirmStatement.confirm(AjaxConfirmButton.this).append("event.preventDefault();"));
			}
		}));
		
		// Lorsque l'évènement 'confirm' est détecté, on déclenche le submit à proprement parler.
		add(new AjaxFormSubmitBehavior(form, "confirm") {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit(AjaxRequestTarget target) {
				AjaxConfirmButton.this.onSubmit(target, AjaxConfirmButton.this.getForm());
			}

			@Override
			protected void onError(AjaxRequestTarget target) {
				AjaxConfirmButton.this.onError(target, AjaxConfirmButton.this.getForm());
			}

			@Override
			public boolean getDefaultProcessing() {
				return AjaxConfirmButton.this.getDefaultFormProcessing();
			}
		});
	}

	@Override
	public void renderHead(IHeaderResponse response) {
		super.renderHead(response);
		response.render(JavaScriptHeaderItem.forReference(BootstrapModalJavaScriptResourceReference.get()));
	}

	protected abstract void onSubmit(AjaxRequestTarget target, Form<?> form);
	
	protected abstract void onError(AjaxRequestTarget target, Form<?> form);
	
	@Override
	public Form<?> getForm() {
		if (form != null) {
			return form;
		} else {
			return super.getForm();
		}
	}

}
