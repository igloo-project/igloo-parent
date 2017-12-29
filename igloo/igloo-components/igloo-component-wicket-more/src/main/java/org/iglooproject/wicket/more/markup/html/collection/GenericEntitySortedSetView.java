package org.iglooproject.wicket.more.markup.html.collection;

import java.util.SortedSet;

import org.apache.wicket.model.IModel;

import org.iglooproject.jpa.business.generic.model.GenericEntity;
import org.iglooproject.wicket.more.markup.repeater.collection.CollectionView;
import org.iglooproject.wicket.more.model.GenericEntityModel;

/**
 * A simple class extending {@link AbstractGenericEntityCollectionView} and allowing to instantiate it without filling in the collection type.
 * @deprecated use {@link CollectionView} with {@link GenericEntityModel} instead : 
 * <pre>
 * {@code 
 * GenericEntity*View<T>("id", model);
 * -> 
 * new CollectionView<T>("id", model, GenericEntityModel.<T>factory());
 * }
 * </pre>
 */
@Deprecated
public abstract class GenericEntitySortedSetView<E extends GenericEntity<?, ?>> extends AbstractGenericEntityCollectionView<E, SortedSet<? extends E>> {

	private static final long serialVersionUID = -7384121758155351030L;

	public GenericEntitySortedSetView(String id, IModel<? extends SortedSet<? extends E>> model) {
		super(id, model);
	}

}
