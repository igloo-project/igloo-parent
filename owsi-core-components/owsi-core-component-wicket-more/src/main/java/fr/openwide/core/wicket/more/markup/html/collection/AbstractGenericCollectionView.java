package fr.openwide.core.wicket.more.markup.html.collection;

import java.util.Collection;

import org.apache.wicket.markup.repeater.RefreshingView;
import org.apache.wicket.model.IModel;

public abstract class AbstractGenericCollectionView<T, C extends Collection<? extends T>>
		extends RefreshingView<T> { // Does not implement IGenericComponent<C> in order to allow using IModel<? extends C>, not only IModel<C>

	private static final long serialVersionUID = 6410475416792396400L;

	public AbstractGenericCollectionView(String id, IModel<? extends C> model) {
		super(id, model);
	}

	@SuppressWarnings("unchecked")
	public IModel<? extends C> getModel() {
		return (IModel<C>) getDefaultModel();
	}

	@SuppressWarnings("unchecked")
	public C getModelObject() {
		return (C) getDefaultModelObject();
	}

}