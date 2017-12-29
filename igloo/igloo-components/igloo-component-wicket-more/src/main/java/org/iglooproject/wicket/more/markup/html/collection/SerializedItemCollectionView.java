package org.iglooproject.wicket.more.markup.html.collection;

import java.io.Serializable;
import java.util.Collection;

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
public abstract class SerializedItemCollectionView<T extends Serializable> extends AbstractSerializedItemCollectionView<T, Collection<? extends T>> {

	private static final long serialVersionUID = -7918106235945473541L;

	public SerializedItemCollectionView(String id, IModel<? extends Collection<? extends T>> model) {
		super(id, model);
	}

}
