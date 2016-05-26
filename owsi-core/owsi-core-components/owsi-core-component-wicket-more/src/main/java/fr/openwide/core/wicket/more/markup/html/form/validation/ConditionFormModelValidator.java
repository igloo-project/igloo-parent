package fr.openwide.core.wicket.more.markup.html.form.validation;

import java.util.Collection;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;

import fr.openwide.core.wicket.more.condition.Condition;

public class ConditionFormModelValidator implements IFormModelValidator {

	private static final long serialVersionUID = 8629253911243139638L;

	private final Condition condition;
	
	private final Collection<FormComponent<?>> formComponents;

	private final String errorRessourceKey;

	public ConditionFormModelValidator(String errorRessourceKey, Condition condition, FormComponent<?> ... formComponents) {
		this(errorRessourceKey, condition, ImmutableList.<FormComponent<?>>builder().add(formComponents).build());
	}

	public ConditionFormModelValidator(String errorRessourceKey, Condition condition, Collection<FormComponent<?>> formComponents) {
		super();
		this.errorRessourceKey = errorRessourceKey;
		this.condition = condition;
		this.formComponents = ImmutableList.copyOf(formComponents);
	}
	
	@Override
	public void validate(Form<?> form) {
		if (!condition.applies()) {
			form.error(form.getString(errorRessourceKey));
		}
	}

	@Override
	public FormComponent<?>[] getDependentFormComponents() {
		return Iterables.toArray(formComponents, FormComponent.class);
	}

}
