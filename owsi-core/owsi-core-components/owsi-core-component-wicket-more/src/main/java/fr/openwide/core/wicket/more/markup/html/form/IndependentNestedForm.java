package fr.openwide.core.wicket.more.markup.html.form;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;

/**
 * A form that is submitted independently from its parent form: it is only submitted explicitly, and not when its parent form is submitted.
 * <p>Since this is not the default behavior in Wicket, we work it around by making {@link #isEnabled()} return {@code false}
 * when the parent form is submitted (thus the data are actually submitted using HTTP, but Wicket ignores them,
 * both when validating and when updating models).
 */
public class IndependentNestedForm<T> extends Form<T> {

	private static final long serialVersionUID = 5851982240353651488L;

	private transient boolean configured = false;

	public IndependentNestedForm(String id, IModel<T> model) {
		super(id, model);
	}

	public IndependentNestedForm(String id) {
		super(id);
	}

	@Override
	protected void onConfigure() {
		super.onConfigure();
		configured = true;
	}

	@Override
	protected void onAfterRender() {
		super.onAfterRender();
		configured = false;
	}

	@Override
	public boolean isEnabled() {
		if (!super.isEnabled()) {
			return false;
		} else {
			if (configured) {
				return true;
			} else {
				Form<?> parentForm = getParentForm();
				return parentForm == null || !parentForm.isSubmitted();
			}
		}
	}

	@Override
	public Form<?> getRootForm() {
		// Makes sure that only this form will be submitted when using AjaxFormSubmitBehavior (for instance).
		// Wicket's default behavior would be to submit the whole root form, but only take this subform into account (don't know why).
		return this;
	}

	private Form<?> getParentForm() {
		return findParent(Form.class);
	}

}
