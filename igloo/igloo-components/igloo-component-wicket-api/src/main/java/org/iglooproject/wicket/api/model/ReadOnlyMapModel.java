package org.iglooproject.wicket.api.model;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.wicket.Component;
import org.apache.wicket.model.IComponentAssignedModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.IWrapModel;
import org.iglooproject.functional.SerializableFunction2;
import org.iglooproject.wicket.api.ICollectionModel;
import org.iglooproject.wicket.api.IMapModel;
import org.iglooproject.wicket.api.Models;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Streams;

public class ReadOnlyMapModel<K, V, M extends Map<K, V>>
		implements IMapModel<K, V, M>, IComponentAssignedModel<M> {

	private static final long serialVersionUID = -6272545665317639093L;
	
	private final IModel<? extends M> readModel;
	
	private final SerializableFunction2<? super K, ? extends IModel<K>> keyModelFactory;
	
	public static <K, V, M extends Map<K, V>> ReadOnlyMapModel<K, V, M> of(
			IModel<? extends M> model, SerializableFunction2<? super K, ? extends IModel<K>> keyFactory) {
		return new ReadOnlyMapModel<>(model, keyFactory);
	}

	protected ReadOnlyMapModel(IModel<? extends M> readModel, SerializableFunction2<? super K, ? extends IModel<K>> keyModelFactory) {
		super();
		this.readModel = checkNotNull(readModel);
		this.keyModelFactory = checkNotNull(keyModelFactory);
	}

	@Override
	public M getObject() {
		return readModel.getObject();
	}
	
	@Override
	public void setObject(M object) {
		throw newReadOnlyException();
	}
	
	private UnsupportedOperationException newReadOnlyException() {
		return new UnsupportedOperationException("Model " + getClass() + " is read-only");
	}

	@Override
	public void detach() {
		readModel.detach();
	}
	
	@Override
	public ICollectionModel<K, Set<K>> keysModel() {
		return new KeysModel();
	}
	
	protected class KeysModel extends AbstractMapCollectionModel<K, Set<K>, IModel<K>> {
		private static final long serialVersionUID = 1L;

		@Override
		public void detach() {
			ReadOnlyMapModel.this.detach();
		}
		@Override
		public Set<K> getObject() {
			Map<K, ?> map = ReadOnlyMapModel.this.getObject();
			return map == null ? null : map.keySet();
		}

		@Override
		protected Iterable<IModel<K>> internalIterable() {
			return keyModelIterable();
		}
	}
	
	private Iterable<IModel<K>> keyModelIterable() {
		M map = readModel.getObject();
		if (map == null) {
			return ImmutableSet.<IModel<K>>of();
		} else {
			return map.keySet().stream().map(keyModelFactory).collect(Collectors.toList());
		}
	}
	
	@Override
	public ICollectionModel<V, Collection<V>> valuesModel() {
		return new ValuesModel();
	}
	
	protected class ValuesModel extends AbstractMapCollectionModel<V, Collection<V>, IModel<V>> {
		private static final long serialVersionUID = 1L;

		@Override
		public void detach() {
			ReadOnlyMapModel.this.detach();
		}
		@Override
		public Collection<V> getObject() {
			Map<?, V> map = ReadOnlyMapModel.this.getObject();
			return map == null ? null : map.values();
		}

		@Override
		protected Iterable<IModel<V>> internalIterable() {
			M map = readModel.getObject();
			if (map == null) {
				return ImmutableSet.<IModel<V>>of();
			} else {
				return Streams.stream(keyModelIterable())
						.map(input -> valueModel(input))
						::iterator;
			}
		}
	}
	
	@Override
	public IModel<V> valueModel(IModel<? extends K> keyModel) {
		return Models.mapModelValueModel(this, keyModel);
	}

	@Override
	public IModel<V> valueModelForProvidedKeyModel(IModel<K> keyModel) {
		return valueModel(keyModel);
	}
	
	@Override
	public Iterator<? extends IModel<K>> iterator(long offset, long limit) {
		return keysModel().iterator(offset, limit);
	}

	@Override
	public long size() {
		M map = readModel.getObject();
		if (map == null) {
			return 0L;
		} else {
			return map.size();
		}
	}

	@Override
	public void put(K key, V value) {
		throw newReadOnlyException();
	}

	@Override
	public void remove(K key) {
		throw newReadOnlyException();
	}

	@Override
	public void clear() {
		throw newReadOnlyException();
	}

	@Override
	public IWrapModel<M> wrapOnAssignment(Component component) {
		return new WrapModel(component);
	}
	
	private class WrapModel extends ReadOnlyMapModel<K, V, M> implements IWrapModel<M> {
		private static final long serialVersionUID = 7996314523359141428L;
		
		protected WrapModel(Component component) {
			super(Models.wrap(readModel, component), keyModelFactory);
		}
		
		@Override
		public IModel<?> getWrappedModel() {
			return ReadOnlyMapModel.this;
		}
	}

}
