package fr.openwide.core.wicket.more.markup.html.collection;

import java.io.Serializable;
import java.util.SortedSet;

import org.apache.wicket.model.IModel;

/**
 * A simple class extending {@link AbstractSerializedItemCollectionView} and allowing to instanciate it without filling in the collection type.
 */
public abstract class SerializedItemSortedSetView<T extends Serializable> extends AbstractSerializedItemCollectionView<T, SortedSet<? extends T>> {

	private static final long serialVersionUID = -5856994024752148564L;

	public SerializedItemSortedSetView(String id, IModel<? extends SortedSet<? extends T>> model) {
		super(id, model);
	}

}
