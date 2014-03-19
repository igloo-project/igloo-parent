package fr.openwide.core.wicket.markup.html.form;

import java.util.Collection;
import java.util.List;

import org.apache.wicket.model.IModel;

import com.google.common.base.Supplier;

import fr.openwide.core.commons.util.functional.Suppliers2;
import fr.openwide.core.wicket.markup.html.model.ConcreteCollectionToCollectionWrapperModel;

public class CheckGroup<T> extends org.apache.wicket.markup.html.form.CheckGroup<T> {

	private static final long serialVersionUID = -4650865960701373800L;

	public CheckGroup(String id) {
		super(id);
	}

	public CheckGroup(String id, Collection<T> collection) {
		super(id, collection);
	}

	public CheckGroup(String id, IModel<List<T>> model) {
		this(id, model, Suppliers2.<T>arrayList());
	}

	public <C extends Collection<T>> CheckGroup(String id, IModel<C> model, Supplier<? extends C> collectionSupplier) {
		super(id, new ConcreteCollectionToCollectionWrapperModel<T, C>(model, collectionSupplier));
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