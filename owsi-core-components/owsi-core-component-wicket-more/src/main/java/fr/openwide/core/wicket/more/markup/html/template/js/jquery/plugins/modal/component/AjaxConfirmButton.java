package fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.modal.component;

import org.apache.wicket.ajax.AjaxChannel;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.string.AppendingStringBuffer;
import org.apache.wicket.util.string.Strings;

import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.modal.behavior.AjaxConfirmSubmitBehavior;
import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.modal.behavior.ConfirmContentBehavior;

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
		add(new AjaxConfirmSubmitBehavior(form, "onclick") {
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
			protected CharSequence getEventHandler() {
				final String script = AjaxConfirmButton.this.getOnClickScript();
				
				AppendingStringBuffer handler = new AppendingStringBuffer();
				
				if (!Strings.isEmpty(script)) {
					handler.append(script).append(";");
				}
				
				handler.append(super.getEventHandler());
				handler.append("; return false;");
				return handler;
			}

			@Override
			protected AjaxChannel getChannel() {
				return AjaxConfirmButton.this.getChannel();
			}

			@Override
			public boolean getDefaultProcessing() {
				return AjaxConfirmButton.this.getDefaultFormProcessing();
			}
		});
	}
	
	protected AjaxChannel getChannel() {
		return null;
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
