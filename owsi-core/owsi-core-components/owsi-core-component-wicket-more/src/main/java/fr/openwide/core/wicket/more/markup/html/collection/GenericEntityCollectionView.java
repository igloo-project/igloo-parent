package fr.openwide.core.wicket.more.markup.html.collection;

import java.util.Collection;

import org.apache.wicket.model.IModel;

import fr.openwide.core.jpa.business.generic.model.GenericEntity;
import fr.openwide.core.wicket.more.markup.repeater.collection.CollectionView;
import fr.openwide.core.wicket.more.model.GenericEntityModel;

/**
 * A simple class extending {@link AbstractGenericEntityCollectionView} and allowing to instantiate it without filling in the collection type.
 * @deprecated use {@link CollectionView} with {@link GenericEntityModel} instead : 
 * <pre>
 * {@code 
 * GenericEntity*View<T>("id", model);
 * -> 
 * new CollectionView<T>("id", model, GenericEntityModel.<T>factory());
 * }
 * </pre>
 */
@Deprecated
public abstract class GenericEntityCollectionView<E extends GenericEntity<?, ?>> extends AbstractGenericEntityCollectionView<E, Collection<? extends E>> {

	private static final long serialVersionUID = 7778450085654200497L;

	public GenericEntityCollectionView(String id, IModel<? extends Collection<? extends E>> model) {
		super(id, model);
	}

}
