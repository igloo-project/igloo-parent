package org.iglooproject.wicket.more.util.listener;

import java.io.Serializable;

import org.apache.wicket.Component;
import org.apache.wicket.MetaDataKey;
import org.apache.wicket.ajax.form.AjaxFormSubmitBehavior;
import org.apache.wicket.application.IComponentInstantiationListener;
import org.apache.wicket.application.IComponentOnAfterRenderListener;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.IFormSubmitter;
import org.apache.wicket.markup.html.form.validation.IFormValidator;
import org.apache.wicket.protocol.http.WebApplication;

public final class FormProcessedListener implements IComponentInstantiationListener, IComponentOnAfterRenderListener {
	
	private static final FormProcessedListener INSTANCE = new FormProcessedListener();
	
	private static final MetaDataKey<Serializable> IS_PROCESSED = new MetaDataKey<Serializable>() {
		private static final long serialVersionUID = 1L;
		private Object readResolve() {
			return IS_PROCESSED;
		}
	};
	
	private static final IFormValidator PROCESSING_LISTENER = new IFormValidator() {
		private static final long serialVersionUID = 1L;
		@Override
		public FormComponent<?>[] getDependentFormComponents() {
			return null;
		}
		@Override
		public void validate(Form<?> form) {
			form.setMetaData(IS_PROCESSED, IS_PROCESSED);
		}
		private Object readResolve() {
			return PROCESSING_LISTENER;
		}
	};
	
	public static boolean isProcessed(Form<?> form) {
		return form.getMetaData(IS_PROCESSED) != null;
	}
	/**
	 * Finds the processed {@link Form} for a given {@link FormComponent}.
	 * 
	 * <h4>Implementation details</h4>
	 * <p>There is a slight difference between a form being submitted and a form being processed:
	 * when a button submits a nested form, it actually submits the whole form (all the data is transfered to the server)
	 * but only triggers the processing of the nested form (data submitted for the root form, for instance, is ignored).
	 * <p>We cannot rely on the isSubmitted() method to determine if a form has been processed,
	 * because it may return true for every sub-form of a root form, even though only one
	 * sub-form has been processed, due to the behavior of AjaxFormSubmitBehavior
	 * that always call <code>form.getRootForm().onFormSubmitted()</code> and not
	 * <code>form.onFormSubmitted()</code>.
	 * @see AjaxFormSubmitBehavior#onEvent(Component, org.apache.wicket.event.IEvent)
	 */
	public static Form<?> findProcessedForm(FormComponent<?> formComponent) {
		final Form<?> form = formComponent.getForm();
		if (form == null) {
			return null;
		}
		final Form<?> rootForm = form.getRootForm();
		IFormSubmitter submitter = rootForm.findSubmitter();
		if (submitter == null) {
			return form;
		} else {
			final Form<?> targetedForm = submitter.getForm();
			if (targetedForm == null) {
				throw new IllegalStateException("submitting component must not return 'null' on getForm()");
			}

			if (targetedForm.equals(rootForm)) {
				// the submitting component points at the root form => so let's
				// just go with
				// root, everything else will be processed with it anyway.
				return rootForm;
			} else {
				// a different form was targeted. let's find the outermost form
				// that wants to be processed.
				Form<?> formThatWantsToBeProcessed = targetedForm;
				Form<?> current = targetedForm.findParent(Form.class);
				while (current != null) {
					if (isProcessed(current)) {
						formThatWantsToBeProcessed = current;
					}
					current = current.findParent(Form.class);
				}
				return formThatWantsToBeProcessed;
			}
		}
	}
	
	public static void init(WebApplication application) {
		application.getComponentInstantiationListeners().add(INSTANCE);
		application.getComponentOnAfterRenderListeners().add(INSTANCE);
	}

	@Override
	public void onInstantiation(Component component) {
		if (component instanceof Form) {
			Form<?> form = (Form<?>) component;
			form.add(PROCESSING_LISTENER);
		}
	}

	@Override
	public void onAfterRender(Component component) {
		if (component instanceof Form) {
			// Nettoyage des metadonnées ajoutées par PROCESSING_LISTENER
			component.setMetaData(IS_PROCESSED, null);
		}
	}
}
