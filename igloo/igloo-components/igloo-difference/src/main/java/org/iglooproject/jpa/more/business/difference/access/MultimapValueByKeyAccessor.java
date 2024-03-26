package org.iglooproject.jpa.more.business.difference.access;

import java.util.Map;

import com.google.common.collect.Multimap;

import de.danielbechler.diff.access.Accessor;
import de.danielbechler.diff.selector.ElementSelector;
import org.iglooproject.jpa.more.business.difference.selector.MapValueByKeySelector;

public class MultimapValueByKeyAccessor<K> implements Accessor, IKeyAwareAccessor<K> {

	private final K key;

	public MultimapValueByKeyAccessor(K key) {
		super();
		this.key = key;
	}

	@Override
	public K getKey() {
		return key;
	}

	@Override
	public ElementSelector getElementSelector() {
		return new MapValueByKeySelector<>(key);
	}

	@SuppressWarnings("rawtypes")
	protected Map objectToMap(Object object) {
		if (object == null) {
			return null;
		} else if (object instanceof Multimap) {
			return ((Multimap) object).asMap();
		} else {
			throw new IllegalArgumentException("Expecting a Multimap");
		}
	}

	@Override
	public Object get(final Object target) {
		final Map<?, ?> targetMap = objectToMap(target);
		if (targetMap != null) {
			return targetMap.get(key);
		}
		return null;
	}

	@SuppressWarnings({"rawtypes", "unchecked"})
	@Override
	public void set(final Object target, final Object value) {
		final Map targetMap = objectToMap(target);
		if (targetMap != null) {
			targetMap.put(key, value);
		}
	}

	@Override
	public void unset(final Object target) {
		final Map<?, ?> targetMap = objectToMap(target);
		if (targetMap != null) {
			targetMap.remove(key);
		}
	}

}
