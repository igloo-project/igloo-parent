package fr.openwide.core.wicket.more.markup.html.collection;

import java.io.Serializable;
import java.util.Set;

import org.apache.wicket.model.IModel;

/**
 * A simple class extending {@link AbstractSerializedItemCollectionView} and allowing to instanciate it without filling in the collection type.
 */
public abstract class SerializedItemSetView<T extends Serializable> extends AbstractSerializedItemCollectionView<T, Set<? extends T>> {

	private static final long serialVersionUID = 6483659206186358747L;

	public SerializedItemSetView(String id, IModel<? extends Set<? extends T>> model) {
		super(id, model);
	}

}
