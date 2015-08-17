package fr.openwide.core.wicket.more.util.validate.validators;

import javax.annotation.Nonnull;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.validation.AbstractFormValidator;
import org.apache.wicket.model.IModel;
import org.apache.wicket.validation.ValidationError;

import fr.openwide.core.commons.util.CloneUtils;

public abstract class AbstractUnicityFormValidator<T> extends AbstractFormValidator {
	
	private static final long serialVersionUID = 3673024433333068895L;

	private final FormComponent<?>[] components;

	private final IModel<? extends T> mainObjectModel;
	
	private final String errorKey;

	public AbstractUnicityFormValidator(IModel<? extends T> mainObjectModel, String errorKey, FormComponent<?>... components) {
		super();
		this.mainObjectModel = mainObjectModel;
		this.errorKey = errorKey;
		this.components = CloneUtils.clone(components);
	}
	
	@Override
	public void detach(Component component) {
		super.detach(component);
		mainObjectModel.detach();
	}

	@Override
	public FormComponent<?>[] getDependentFormComponents() {
		return CloneUtils.clone(components);
	}

	@Override
	public void validate(Form<?> form) {
		final FormComponent<?> formComponent1 = components[0];
		
		T mainObject = mainObjectModel.getObject();
		T matchingObject = getByUniqueField();
		if (matchingObject != null && !isSame(matchingObject, mainObject)) {
			error(formComponent1, errorKey);
		}
	}
	
	protected boolean isSame(@Nonnull T matchingObject, T mainObject) {
		return matchingObject.equals(mainObject);
	}

	protected ValidationError decorate(ValidationError error) {
		// Does nothing by default
		return error;
	}

	protected abstract T getByUniqueField();

}
