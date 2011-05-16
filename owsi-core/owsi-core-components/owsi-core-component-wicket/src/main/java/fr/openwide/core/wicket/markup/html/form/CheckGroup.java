package fr.openwide.core.wicket.markup.html.form;

import java.util.Collection;

import org.apache.wicket.model.IModel;

public class CheckGroup<T> extends org.apache.wicket.markup.html.form.CheckGroup<T> {

	private static final long serialVersionUID = -4650865960701373800L;

	public CheckGroup(String id) {
		super(id);
	}

	public CheckGroup(String id, Collection<T> collection) {
		super(id, collection);
	}

	public CheckGroup(String id, IModel<? extends Collection<T>> model) {
		super(id, model);
	}

	@Override
	public void updateModel() {
		if (getModelObject() == null) {
			setDefaultModelObject(getConvertedInput());
		} else {
			modelChanging();
			setDefaultModelObject(getConvertedInput());
			modelChanged();
		}
	}

}