package fr.openwide.core.wicket.more.util.model;

import java.util.Map;

import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IDetachable;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.StringResourceModel;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

public final class Models {

	private Models() {
	}

	@SuppressWarnings("unchecked") // ModelGetObjectFunction works for any T
	public static <T> Function<? super IModel<? extends T>, T> getObject() {
		return (Function<? super IModel<? extends T>, T>) ModelGetObjectFunction.INSTANCE;
	}
	
	@SuppressWarnings("rawtypes") // ModelGetObjectFunction works for any T
	private enum ModelGetObjectFunction implements Function<IModel, Object> {
		INSTANCE;
		
		@Override
		public Object apply(IModel input) {
			return input == null ? null : input.getObject();
		}
	}

	/**
	 * A "placeholder" model, for use when the actual behavior of getObject() and setObject() do not matter.
	 * 
	 * <p>{@link #getObject()} always returns null and {@link #setObject(Object)} and {@link #detach()} do nothing.
	 */
	@SuppressWarnings("unchecked") // Works for any T
	public static <T> IModel<T> placeholder() {
		return (IModel<T>) PlaceholderModel.INSTANCE;
	}
		
	private enum PlaceholderModel implements IModel<Object> {
		INSTANCE;
		private Object readResolve() {
			return INSTANCE;
		}
		@Override
		public Object getObject() {
			return null;
		}
		
		@Override
		public void setObject(Object object) {
			// Does nothing
		}
		
		@Override
		public void detach() {
			// Does nothing
		}
	};
	
	/**
	 * A static model of <code>Map<String, Object></code>. Useful as a data model for {@link StringResourceModel}.
	 */
	public static MapModelBuilder<String, Object> dataMap() {
		return new MapModelBuilder<String, Object>();
	}
	
	public static class MapModelBuilder<K, V> {
		private ImmutableMap.Builder<K, IModel<? extends V>> delegate = ImmutableMap.builder();
		public MapModelBuilder<K, V> put(K key, V value) {
			delegate.put(key, transientModel(value));
			return this;
		}
		public MapModelBuilder<K, V> put(K key, IModel<? extends V> valueModel) {
			delegate.put(key, valueModel);
			return this;
		}
		public IModel<Map<K, V>> build() {
			return new MapModelBuilderMap<>(delegate.build());
		}

		private static class MapModelBuilderMap<K, V> extends LoadableDetachableModel<Map<K, V>> {
			private static final long serialVersionUID = 1L;
			
			private Map<K, IModel<? extends V>> source;
			
			public MapModelBuilderMap(Map<K, IModel<? extends V>> source) {
				super();
				this.source = source;
			}
			
			@Override
			protected Map<K, V> load() {
				return Maps.transformValues(source, Models.<V>getObject());
			}
			
			@Override
			protected void onDetach() {
				super.onDetach();
				for (IDetachable detachable : source.values()) {
					detachable.detach();
				}
			}
		}
	}

	/**
	 * A constant, non-serializable model.
	 * <p>Useful when calling 
	 */
	public static <T> IModel<T> transientModel(T value) {
		return new TransientModel<>(value);
	}
	
	private static class TransientModel<T> extends AbstractReadOnlyModel<T> {
		
		private static final long serialVersionUID = -2160512073899616819L;
		
		private final T value;
		
		public TransientModel(T value) {
			super();
			this.value = value;
		}
		
		@Override
		public T getObject() {
			return value;
		}
		
	}
}
