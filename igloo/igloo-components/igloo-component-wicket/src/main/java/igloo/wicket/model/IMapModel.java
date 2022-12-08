package igloo.wicket.model;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import org.apache.wicket.model.IModel;

/**
 * A {@link ISequenceProvider} that also is a model of map, and that provides some write operations.
 * 
 * <p>This model also allows a full iteration on its underlying item models, and provides {@link ICollectionModel}s
 * for its keys and values.
 * 
 * @author yrodiere
 *
 * @param <T> The element type.
 * @param <M> The map type.
 */
public interface IMapModel<K, V, M extends Map<K, V>>
		extends IModel<M>, IMapProvider<K, V> {

	/**
	 * @return A mostly read-only wrapper model that will dynamically return the keys of the underlying map.
	 * This model delegates calls to {@link #clear()} to the map model, but no other write operation is allowed.
	 */
	ICollectionModel<K, Set<K>> keysModel();

	/**
	 * @return A mostly read-only wrapper model that will dynamically return the values of the underlying map.
	 * This model delegates calls to {@link #clear()} to the map model, but no other write operation is allowed.
	 */
	ICollectionModel<V, Collection<V>> valuesModel();
	
	void put(K key, V value);
	
	void remove(K key);
	
	void clear();

}
