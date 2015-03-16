package fr.openwide.core.wicket.more.markup.html.form.validation;

import java.util.Collection;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.validation.AbstractFormValidator;
import org.apache.wicket.util.string.Strings;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterables;

public class OneRequiredFormValidator extends AbstractFormValidator {
	
	private static final long serialVersionUID = -7019352590059451557L;
	
	private final Collection<FormComponent<?>> requiredFormComponents;
	
	public OneRequiredFormValidator(FormComponent<?> first, FormComponent<?> second, FormComponent<?> ... rest) {
		this.requiredFormComponents = ImmutableList.<FormComponent<?>>builder().add(first, second).add(rest).build();
	}
	
	@Override
	public final FormComponent<?>[] getDependentFormComponents() {
		return Iterables.toArray(
				Iterables.concat(requiredFormComponents, getAdditionalDependentFormComponents()),
				FormComponent.class
		);
	}
	
	protected Iterable<? extends FormComponent<?>> getAdditionalDependentFormComponents() {
		return ImmutableList.of();
	}

	@Override
	public void validate(Form<?> form) {
		if (isEnabled(form)) {
			for (FormComponent<?> formComponent : requiredFormComponents) {
				if (checkRequired(formComponent)) {
					return;
				}
			}
			
			onError(form);
		}
	}
	
	protected void onError(Form<?> form) {
		Joiner labelJoiner = Joiner.on(form.getString("common.validator.oneRequired.labels.separator"));
		Object labels = labelJoiner.join(Iterables.transform(requiredFormComponents, new Function<FormComponent<?>, String>() {
			@Override
			public String apply(FormComponent<?> input) {
				return input.getLabel().getObject();
			}
		}));
		
		error(requiredFormComponents.iterator().next(), "common.validator.oneRequired", ImmutableMap.of("labels", labels));
	}
	
	/**
	 * Code copied from FormComponent#checkRequired, which is unfortunately useless as long as FormComponent#required is false.
	 */
	protected boolean checkRequired(FormComponent<?> formComponent) {
		final String input = formComponent.getInput();

		// when null, check whether this is natural for that component, or
		// whether - as is the case with text fields - this can only happen
		// when the component was disabled
		if (input == null && !formComponent.isInputNullable() && !formComponent.isEnabledInHierarchy())
		{
			// this value must have come from a disabled field
			// do not perform validation
			return true;
		}

		// peform validation by looking whether the value is null or empty
		return !Strings.isEmpty(input);
	}

}
