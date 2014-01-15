package fr.openwide.core.wicket.more.markup.html.collection;

import java.util.Collection;

import org.apache.wicket.model.IModel;

import fr.openwide.core.jpa.business.generic.model.GenericEntity;

/**
 * A simple class extending {@link AbstractGenericEntityCollectionView} and allowing to instanciate it without filling in the collection type.
 */
public abstract class GenericEntityCollectionView<E extends GenericEntity<?, ?>> extends AbstractGenericEntityCollectionView<E, Collection<? extends E>> {

	private static final long serialVersionUID = 7778450085654200497L;

	public GenericEntityCollectionView(String id, IModel<Collection<? extends E>> model) {
		super(id, model);
	}

}
