package fr.openwide.core.wicket.more.markup.html.collection;

import java.util.Collection;

import org.apache.wicket.model.IModel;

import fr.openwide.core.jpa.business.generic.model.GenericEntity;

/**
 * A simple class extending {@link AbstractSerializedItemCollectionView} and allowing to instanciate it without filling in the collection type.
 */
public abstract class SerializedItemCollectionView<E extends GenericEntity<?, ?>> extends AbstractSerializedItemCollectionView<E, Collection<? extends E>> {

	private static final long serialVersionUID = -7918106235945473541L;

	public SerializedItemCollectionView(String id, IModel<? extends Collection<? extends E>> model) {
		super(id, model);
	}

}
