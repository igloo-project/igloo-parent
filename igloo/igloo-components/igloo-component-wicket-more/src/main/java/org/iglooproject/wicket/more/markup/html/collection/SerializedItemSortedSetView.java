package org.iglooproject.wicket.more.markup.html.collection;

import java.io.Serializable;
import java.util.SortedSet;

import org.apache.wicket.model.IModel;

import org.iglooproject.wicket.more.markup.repeater.collection.CollectionView;
import org.iglooproject.wicket.more.util.model.Models;

/**
 * A simple class extending {@link AbstractSerializedItemCollectionView} and allowing to instantiate it without filling in the collection type.
 * @deprecated use {@link CollectionView} with {@link Models} instead : 
 * <pre>
 * {@code 
 * new SerializedItem*View<T>("id", model);
 * ->
 * new CollectionView<T>("id", model, Models.<T>serializableModelFactory());
 * }
 * </pre>
 */
@Deprecated
public abstract class SerializedItemSortedSetView<T extends Serializable> extends AbstractSerializedItemCollectionView<T, SortedSet<? extends T>> {

	private static final long serialVersionUID = -5856994024752148564L;

	public SerializedItemSortedSetView(String id, IModel<? extends SortedSet<? extends T>> model) {
		super(id, model);
	}

}
