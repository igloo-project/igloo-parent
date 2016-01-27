package fr.openwide.core.wicket.more.markup.repeater.map;

import java.util.Iterator;
import java.util.Map;

import org.apache.wicket.model.IModel;

/**
 * A {@link IMapModel} that provides a specific type of models for its items (keys and values).
 * 
 * <p>This model also allows a full iteration on its underlying key models.
 */
public interface IItemModelAwareMapModel<K, V, M extends Map<K, V>, MK extends IModel<K>, MV extends IModel<V>>
		extends IMapModel<K, V, M>, Iterable<MK> {
	
	@Override
	public Iterator<MK> iterator(long offset, long limit);
	
	@Override
	public MV getValueModelForProvidedKeyModel(IModel<K> keyModel);

}
