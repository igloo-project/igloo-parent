package fr.openwide.core.wicket.more.markup.html.select2;

import java.util.Collection;

import org.apache.wicket.markup.html.form.AbstractTextComponent;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.ListMultipleChoice;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.IObjectClassAwareModel;
import org.apache.wicket.util.convert.ConversionException;
import org.retzlaff.select2.AbstractSelect2Choice;
import org.retzlaff.select2.ISelect2AjaxAdapter;

import fr.openwide.core.wicket.more.model.BindingModel;

public abstract class AbstractSelect2MultipleChoice<C extends Collection<T>, T> extends AbstractSelect2Choice<C, T> {
	
	private static final long serialVersionUID = -4183760466356487637L;
	
	protected static final String SELECT2_VALUE_SEPARATOR = ",";

	protected AbstractSelect2MultipleChoice(String id, IModel<C> model, ISelect2AjaxAdapter<T> adapter) {
		super(id, model, adapter, true);
	}
	
	protected abstract C newEmptyImplementation();

	@Override
	protected Collection<T> getModelObjects() {
		Collection<T> collection = getModelObject();
		
		if (collection == null) {
			return newEmptyImplementation();
		}
		
		return collection;
	}
	
	@Override
	public void updateModel() {
		C convertedInput = getConvertedInput();

		modelChanging();
		getModel().setObject(convertedInput);
		modelChanged();
	}
	
	/**
	 * @see ListMultipleChoice#getModelValue()
	 */
	@Override
	protected String getModelValue() {
		Collection<T> collection = getModelObject();
		if (collection == null || collection.isEmpty()) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		for (T object : collection) {
			sb.append(getAdapter().getChoiceId(object)).append(SELECT2_VALUE_SEPARATOR);
		}
		sb.setLength(sb.length() - 1); // trailing comma
		return sb.toString();
	}
	
	/**
	 * Workaround.<br>
	 * When the underlying model is a {@link IObjectClassAwareModel} (a {@link BindingModel} for example),
	 * the {@link AbstractTextComponent#resolveType()} method will be able to determine the model type.<br>
	 * As a consequence, the {@link FormComponent#convertInput()} method will totally ignore {@link Select2SingleChoice#convertValue(String[]))},
	 * which is quite annoying since this is the method that makes use of {@link ISelect2AjaxAdapter#getChoice(String)}...<br>
	 * That's why we have to force the use of {@link #convertValue(String[])}.
	 */
	@Override
	protected void convertInput() {
		try {
			setConvertedInput(convertValue(getInputAsArray()));
		} catch (ConversionException e) {
			error(newValidationError(e));
		}
	}
	
	/**
	 * @see ListMultipleChoice#convertValue().
	 */
	@Override
	protected C convertValue(final String[] value) {
		if (value == null || value.length == 0 || value[0] == null) {
			return newEmptyImplementation();
		}

		C collection = newEmptyImplementation();
		String[] ids = value[0].split(SELECT2_VALUE_SEPARATOR);
		for (String id : ids) {
			collection.add(getAdapter().getChoice(id));
		}
		return collection;
	}

}