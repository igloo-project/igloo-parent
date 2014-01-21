package fr.openwide.core.wicket.more.markup.html.collection;

import java.util.Set;

import org.apache.wicket.model.IModel;

import fr.openwide.core.jpa.business.generic.model.GenericEntity;

/**
 * A simple class extending {@link AbstractSerializedItemCollectionView} and allowing to instanciate it without filling in the collection type.
 */
public abstract class SerializedItemSetView<E extends GenericEntity<?, ?>> extends AbstractSerializedItemCollectionView<E, Set<? extends E>> {

	private static final long serialVersionUID = 6483659206186358747L;

	public SerializedItemSetView(String id, IModel<? extends Set<? extends E>> model) {
		super(id, model);
	}

}
