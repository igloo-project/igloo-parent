package fr.openwide.core.wicket.more.markup.html.collection;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

import org.apache.wicket.IGenericComponent;
import org.apache.wicket.markup.repeater.RefreshingView;
import org.apache.wicket.markup.repeater.util.ModelIteratorAdapter;
import org.apache.wicket.model.IModel;

import fr.openwide.core.jpa.business.generic.model.GenericEntity;
import fr.openwide.core.wicket.more.model.GenericEntityModel;

/**
 * A {@link RefreshingView} that can be used for any type of {@link GenericEntity} collection.
 * <p>While this view supports lists, you may want to use {@link GenericEntityListView} when possible, since it provides more functionalities.
 * @see GenericEntityCollectionView
 * @see GenericEntitySetView
 * @see GenericEntitySortedSetView
 * @see GenericEntityListView
 */
public abstract class AbstractGenericEntityCollectionView<T extends GenericEntity<?, ?>, C extends Collection<? extends T>>
		extends RefreshingView<T> implements IGenericComponent<C> {

	private static final long serialVersionUID = 1L;

	public AbstractGenericEntityCollectionView(String id, IModel<C> model) {
		super(id, model);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public IModel<C> getModel() {
		return (IModel<C>) getDefaultModel();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public C getModelObject() {
		return (C) getDefaultModelObject();
	}
	
	@Override
	public void setModel(IModel<C> model) {
		setDefaultModel(model);
	}
	
	@Override
	public void setModelObject(C object) {
		setDefaultModelObject(object);
	}

	/**
	 * Note: if you wish to override this, and overriding {@link #getModel(GenericEntity)} is not enough, you're better off extending {@link RefreshingView} directly.
	 */
	@Override
	protected final Iterator<IModel<T>> getItemModels() {
		Collection<T> collectionWithoutTypeWildcard = Collections.unmodifiableCollection(getModelObject());
		return new ModelIteratorAdapter<T>(collectionWithoutTypeWildcard.iterator()) {
			@Override
			protected IModel<T> model(T object) {
				return AbstractGenericEntityCollectionView.this.getModel(object);
			}
		};
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" }) // Works around restrictions on GenericEntityModel that seem too strong.
	public IModel<T> getModel(T object) {
		return new GenericEntityModel(object);
	}

}
