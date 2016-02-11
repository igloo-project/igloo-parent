package fr.openwide.core.wicket.more.markup.repeater.map;

import java.util.Map;

import org.apache.wicket.model.IModel;

public interface IMapModel<K, V, M extends Map<K, V>> extends IModel<M>, IMapProvider<K, V> {
	
	void put(K key, V value);
	
	void remove(K key);
	
	void clear();

}
