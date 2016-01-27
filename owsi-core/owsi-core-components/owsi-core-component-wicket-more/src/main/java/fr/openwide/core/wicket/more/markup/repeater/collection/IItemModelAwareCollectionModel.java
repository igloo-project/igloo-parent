package fr.openwide.core.wicket.more.markup.repeater.collection;

import java.util.Collection;
import java.util.Iterator;

import org.apache.wicket.model.IModel;

/**
 * A {@link ICollectionModel} that provides a specific type of models for its items.
 * 
 * <p>This model also allows a full iteration on its underlying item models.
 */
public interface IItemModelAwareCollectionModel<T, C extends Collection<T>, M extends IModel<T>>
		extends ICollectionModel<T, C>, Iterable<M> {
	
	@Override
	public Iterator<M> iterator(long offset, long limit);

}
