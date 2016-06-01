package fr.openwide.core.wicket.more.markup.html.form.validation;

import java.util.Collection;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;

import fr.openwide.core.wicket.more.condition.Condition;
import fr.openwide.core.wicket.more.util.model.Models;

public class ConditionFormModelValidator implements IFormModelValidator {

	private static final long serialVersionUID = 8629253911243139638L;

	private final Condition condition;
	
	private final Collection<FormComponent<?>> formComponents;

	private final IModel<String> errorMessageModel;

	public ConditionFormModelValidator(String errorRessourceKey, Condition condition, FormComponent<?> ... formComponents) {
		this(errorRessourceKey, condition, ImmutableList.<FormComponent<?>>builder().add(formComponents).build());
	}

	public ConditionFormModelValidator(String errorRessourceKey, Condition condition, Collection<FormComponent<?>> formComponents) {
		this(new ResourceModel(errorRessourceKey), condition, formComponents);
	}

	public ConditionFormModelValidator(IModel<String> errorMessageModel, Condition condition, FormComponent<?> ... formComponents) {
		this(errorMessageModel, condition, ImmutableList.<FormComponent<?>>builder().add(formComponents).build());
	}

	public ConditionFormModelValidator(IModel<String> errorMessageModel, Condition condition, Collection<FormComponent<?>> formComponents) {
		this.errorMessageModel = errorMessageModel;
		this.condition = condition;
		this.formComponents = ImmutableList.copyOf(formComponents);
	}
	
	@Override
	public void validate(Form<?> form) {
		if (!condition.applies()) {
			form.error(Models.wrap(errorMessageModel, form).getObject());
		}
	}

	@Override
	public FormComponent<?>[] getDependentFormComponents() {
		return Iterables.toArray(formComponents, FormComponent.class);
	}

	@Override
	public void detach() {
		condition.detach();
		errorMessageModel.detach();
	}

}
