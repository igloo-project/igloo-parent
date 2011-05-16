package fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.fancybox;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;

import fr.openwide.core.wicket.markup.html.panel.GenericPanel;
import fr.openwide.core.wicket.more.markup.html.feedback.ContainerFeedbackPanel;

public abstract class AbstractGenericPopupFormPanel<T> extends GenericPanel<T> implements IPopupContentAwareComponent {

	private static final long serialVersionUID = 1772065899149334374L;
	
	private FormPanelMode formPanelMode;
	
	protected Form<T> itemForm;

	protected ContainerFeedbackPanel feedback;

	public AbstractGenericPopupFormPanel(String id, IModel<T> model, FormPanelMode formPanelMode) {
		super(id, model);
		this.setOutputMarkupId(true);
		
		if (formPanelMode == null) {
			throw new IllegalStateException("GenericFormPanel must have a FormPanelMode");
		}
		this.formPanelMode = formPanelMode;
		
		feedback = new ContainerFeedbackPanel("feedback", this);
		feedback.setOutputMarkupId(true);
		
		itemForm = new Form<T>("itemForm", model);
		
		AjaxButton submitButton = new AjaxButton("submitButton") {
			
			private static final long serialVersionUID = -3721476446722988109L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				AbstractGenericPopupFormPanel.this.onSubmitInternal(target, form);
			}
			
			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				AbstractGenericPopupFormPanel.this.reloadPanel(target);
			}
			
		};
		itemForm.add(submitButton);
		
		add(itemForm, feedback);
	}

	public boolean isAddMode() {
		return formPanelMode == FormPanelMode.ADD;
	}

	public boolean isEditMode() {
		return formPanelMode == FormPanelMode.EDIT;
	}

	protected void reloadPanel(AjaxRequestTarget target) {
		if (target != null) {
			target.addComponent(this);
			target.appendJavascript(FancyboxHelper.getResize());
		}
	}
	
	@Override
	public void hide(AjaxRequestTarget arg0) {
		// Nothing
	}
	
	@Override
	public void show(AjaxRequestTarget arg0) {
		if (isAddMode()) {
			itemForm.setModelObject(getEmptyModelObject());
		}
		itemForm.clearInput();
	}
	
	protected abstract T getEmptyModelObject();
	
	protected abstract void onSubmitInternal(AjaxRequestTarget target, Form<?> form);

}
