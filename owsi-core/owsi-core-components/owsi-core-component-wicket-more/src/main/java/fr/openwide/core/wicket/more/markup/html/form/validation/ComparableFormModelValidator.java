package fr.openwide.core.wicket.more.markup.html.form.validation;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collection;
import java.util.Comparator;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.util.lang.Classes;
import org.apache.wicket.validation.ValidationError;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Ordering;

public class ComparableFormModelValidator<T extends Comparable<T>> implements IFormModelValidator {

	private static final long serialVersionUID = 2750525584310932237L;

	private final Collection<FormComponent<T>> components;

	private final Comparator<T> comparator;

	private final String errorRessourceKey;

	public ComparableFormModelValidator(FormComponent<T> formComponent1, FormComponent<T> formComponent2, String errorKey) {
		this(formComponent1, formComponent2, Ordering.<T>natural(), errorKey);
	}

	public ComparableFormModelValidator(FormComponent<T> formComponent1, FormComponent<T> formComponent2, Comparator<T> comparator, String errorKey) {
		checkNotNull(formComponent1);
		checkNotNull(formComponent2);
		checkNotNull(comparator);
		
		this.components = ImmutableList.of(formComponent1, formComponent2);
		this.comparator = comparator;
		this.errorRessourceKey = errorKey;
	}

	@Override
	public void validate(Form<?> form) {
		FormComponent<T> formComponent1 = Iterables.get(components, 0);
		FormComponent<T> formComponent2 = Iterables.get(components, 1);
		
		if (
				formComponent1.getConvertedInput() != null
			&&	formComponent2.getConvertedInput() != null
			&&	comparator.compare(formComponent1.getConvertedInput(), formComponent2.getConvertedInput()) > 0
		) {
			formComponent2.error(new ValidationError().addKey(errorRessourceKey).addKey(Classes.simpleName(getClass())));
		}
	}

	@Override
	public FormComponent<?>[] getDependentFormComponents() {
		return Iterables.toArray(components, FormComponent.class);
	}

}
