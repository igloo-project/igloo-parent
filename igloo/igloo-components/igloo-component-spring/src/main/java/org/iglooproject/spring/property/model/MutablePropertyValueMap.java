package org.iglooproject.spring.property.model;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import com.google.common.base.Function;
import com.google.common.collect.Iterators;

public class MutablePropertyValueMap implements IMutablePropertyValueMap {
	
	private Map<MutablePropertyId<?>, Object> delegate = new LinkedHashMap<>();

	@Override
	public Iterator<Entry<?>> iterator() {
		return Iterators.transform(
				delegate.entrySet().iterator(),
				new Function<Map.Entry<MutablePropertyId<?>, Object>, IMutablePropertyValueMap.Entry<?>>() {
					@Override
					public Entry<?> apply(Map.Entry<MutablePropertyId<?>, Object> input) {
						return new EntryImpl<>(input);
					}
				}
		);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T get(MutablePropertyId<T> key) {
		return (T) delegate.get(key);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T put(MutablePropertyId<T> key, T value) {
		return (T) delegate.put(key, value);
	}

	@SuppressWarnings("unchecked")
	private static final class EntryImpl<T> implements Entry<T> {
		
		private final Map.Entry<MutablePropertyId<?>, Object> delegate;

		protected EntryImpl(Map.Entry<MutablePropertyId<?>, Object> delegate) {
			super();
			this.delegate = delegate;
		}

		@Override
		public MutablePropertyId<T> getKey() {
			return (MutablePropertyId<T>) delegate.getKey();
		}

		@Override
		public T getValue() {
			return (T) delegate.getValue();
		}
		
	}

}
