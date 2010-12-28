package fr.openwide.core.wicket.markup.html.form;

import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.model.ResourceModel;

public final class FormComponentHelper {

	public static <T> FormComponent<T> setLabel(FormComponent<T> component, String labelId) {
		return component.setLabel(new ResourceModel(labelId));
	}
	
	private FormComponentHelper() {
	}
	
}
