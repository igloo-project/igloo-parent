package fr.openwide.core.wicket.more.markup.html.collection;

import java.util.Collection;

import org.apache.wicket.model.IModel;

import fr.openwide.core.jpa.business.generic.model.GenericEntity;
import fr.openwide.core.wicket.more.model.GenericEntityModel;

/**
 * An {@link AbstractGenericCollectionView} that can be used for any type of {@link GenericEntity} collection.
 * <p>While this view supports lists, you may want to use {@link GenericEntityListView} when possible, since it provides more functionalities.
 * @see GenericEntityCollectionView
 * @see GenericEntitySetView
 * @see GenericEntitySortedSetView
 * @see GenericEntityListView
 */
public abstract class AbstractGenericEntityCollectionView<T extends GenericEntity<?, ?>, C extends Collection<? extends T>>
		extends AbstractGenericCollectionView<T, C> {

	private static final long serialVersionUID = 1L;

	public AbstractGenericEntityCollectionView(String id, IModel<? extends C> model) {
		super(id, model);
	}
	
	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" }) // Works around restrictions on GenericEntityModel that seem too strong.
	protected IModel<T> getItemModel(T object) {
		return new GenericEntityModel(object);
	}

}
