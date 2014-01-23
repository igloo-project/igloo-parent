package fr.openwide.core.wicket.more.markup.html.collection;

import java.util.Set;

import org.apache.wicket.model.IModel;

import fr.openwide.core.jpa.business.generic.model.GenericEntity;

/**
 * A simple class extending {@link AbstractGenericEntityCollectionView} and allowing to instanciate it without filling in the collection type.
 */
public abstract class GenericEntitySetView<E extends GenericEntity<?, ?>> extends AbstractGenericEntityCollectionView<E, Set<? extends E>> {

	private static final long serialVersionUID = -7384121758155351030L;

	public GenericEntitySetView(String id, IModel<? extends Set<? extends E>> model) {
		super(id, model);
	}

}
