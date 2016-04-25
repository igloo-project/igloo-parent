package fr.openwide.core.wicket.more.markup.html.collection;

import java.io.Serializable;
import java.util.Set;

import org.apache.wicket.model.IModel;

import fr.openwide.core.wicket.more.markup.repeater.collection.CollectionView;
import fr.openwide.core.wicket.more.util.model.Models;

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
public abstract class SerializedItemSetView<T extends Serializable> extends AbstractSerializedItemCollectionView<T, Set<? extends T>> {

	private static final long serialVersionUID = 6483659206186358747L;

	public SerializedItemSetView(String id, IModel<? extends Set<? extends T>> model) {
		super(id, model);
	}

}
