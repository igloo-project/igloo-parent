package org.iglooproject.wicket.model;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.wicket.model.IModel;

/**
 * A {@link IMapModel} that provides a specific type of models for its items (keys and values).
 * 
 * <p>This model also allows a full iteration on its underlying key models.
 */
public interface IItemModelAwareMapModel<K, V, M extends Map<K, V>, MK extends IModel<K>, MV extends IModel<V>>
		extends IMapModel<K, V, M>, Iterable<MK> {
	
	@Override 
	Iterator<MK> iterator(long offset, long limit);
	
	@Override
	IItemModelAwareCollectionModel<K, Set<K>, MK> keysModel();
	
	@Override
	IItemModelAwareCollectionModel<V, Collection<V>, MV> valuesModel();
	
	@Override
	MV valueModelForProvidedKeyModel(IModel<K> keyModel);

}
