package fr.openwide.core.wicket.more.markup.html.collection;

import java.io.Serializable;
import java.util.Collection;

import org.apache.wicket.model.IModel;

/**
 * A simple class extending {@link AbstractSerializedItemCollectionView} and allowing to instanciate it without filling in the collection type.
 */
public abstract class SerializedItemCollectionView<T extends Serializable> extends AbstractSerializedItemCollectionView<T, Collection<? extends T>> {

	private static final long serialVersionUID = -7918106235945473541L;

	public SerializedItemCollectionView(String id, IModel<? extends Collection<? extends T>> model) {
		super(id, model);
	}

}
