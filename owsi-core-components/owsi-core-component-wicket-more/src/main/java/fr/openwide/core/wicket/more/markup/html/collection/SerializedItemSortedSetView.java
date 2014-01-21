package fr.openwide.core.wicket.more.markup.html.collection;

import java.util.SortedSet;

import org.apache.wicket.model.IModel;

import fr.openwide.core.jpa.business.generic.model.GenericEntity;

/**
 * A simple class extending {@link AbstractSerializedItemCollectionView} and allowing to instanciate it without filling in the collection type.
 */
public abstract class SerializedItemSortedSetView<E extends GenericEntity<?, ?>> extends AbstractSerializedItemCollectionView<E, SortedSet<? extends E>> {

	private static final long serialVersionUID = -5856994024752148564L;

	public SerializedItemSortedSetView(String id, IModel<? extends SortedSet<? extends E>> model) {
		super(id, model);
	}

}
