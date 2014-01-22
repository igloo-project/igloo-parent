package fr.openwide.core.wicket.more.markup.html.collection;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

import org.apache.wicket.markup.repeater.RefreshingView;
import org.apache.wicket.markup.repeater.util.ModelIteratorAdapter;
import org.apache.wicket.model.IModel;

import com.google.common.collect.Iterators;

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
	
	/**
	 * Note: if you wish to override this, and overriding {@link #getModel(T)} is not enough, you're better off extending {@link RefreshingView} directly.
	 */
	@Override
	protected final Iterator<IModel<T>> getItemModels() {
		C collection = getModelObject();
		if (collection == null) {
			return Iterators.emptyIterator();
		}
		
		Collection<T> collectionWithoutTypeWildcard = Collections.unmodifiableCollection(getModelObject());
		return new ModelIteratorAdapter<T>(collectionWithoutTypeWildcard.iterator()) {
			@Override
			protected IModel<T> model(T object) {
				return AbstractGenericCollectionView.this.getItemModel(object);
			}
		};
	}
	
	public int getViewSize() {
		C collection = getModelObject();
		if (collection == null) {
			return 0;
		}
		return collection.size();
	}
	
	protected abstract IModel<T> getItemModel(T object);

}