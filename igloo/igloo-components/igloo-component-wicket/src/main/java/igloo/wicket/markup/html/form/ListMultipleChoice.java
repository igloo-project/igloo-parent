package igloo.wicket.markup.html.form;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.model.IModel;

import com.google.common.collect.Lists;

public class ListMultipleChoice<T> extends
		org.apache.wicket.markup.html.form.ListMultipleChoice<T> {

	private static final long serialVersionUID = 8054894237478953799L;

	public ListMultipleChoice(String id) {
		super(id);
	}

	public ListMultipleChoice(String id, List<? extends T> choices) {
		super(id, choices);
	}

	public ListMultipleChoice(String id,
			IModel<? extends List<? extends T>> choices) {
		super(id, choices);
	}

	public ListMultipleChoice(String id, List<? extends T> choices, int maxRows) {
		super(id, choices, maxRows);
	}

	public ListMultipleChoice(String id, List<? extends T> choices,
			IChoiceRenderer<? super T> renderer) {
		super(id, choices, renderer);
	}

	public ListMultipleChoice(String id,
			IModel<? extends Collection<T>> object, List<? extends T> choices) {
		super(id, object, choices);
	}

	public ListMultipleChoice(String id, IModel<? extends Collection<T>> model,
			IModel<? extends List<? extends T>> choices) {
		super(id, model, choices);
	}

	public ListMultipleChoice(String id,
			IModel<? extends List<? extends T>> choices,
			IChoiceRenderer<? super T> renderer) {
		super(id, choices, renderer);
	}

	public ListMultipleChoice(String id,
			IModel<? extends Collection<T>> object, List<? extends T> choices,
			IChoiceRenderer<? super T> renderer) {
		super(id, object, choices, renderer);
	}

	public ListMultipleChoice(String id, IModel<? extends Collection<T>> model,
			IModel<? extends List<? extends T>> choices,
			IChoiceRenderer<? super T> renderer) {
		super(id, model, choices, renderer);
	}
	
	@Override
	public void updateModel() {
		Collection<T> convertedInput = getConvertedInput();
		if (convertedInput == null) {
			convertedInput = Collections.emptyList();
		}
		
		if (getModelObject() != null) {
			modelChanging();
			setModelObject(Lists.newArrayList(convertedInput));
			modelChanged();
		} else {
			setModelObject(Lists.newArrayList(convertedInput));
		}
	}

}
