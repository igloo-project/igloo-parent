package igloo.wicket.model;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterables;
import com.google.common.collect.Iterators;
import com.google.common.collect.Maps;
import com.google.common.primitives.Ints;
import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.wicket.Component;
import org.apache.wicket.model.IComponentAssignedModel;
import org.apache.wicket.model.IDetachable;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.iglooproject.commons.util.collections.Iterators2;
import org.iglooproject.functional.SerializableFunction2;
import org.iglooproject.functional.SerializablePredicate2;
import org.iglooproject.functional.SerializableSupplier2;

public final class Models {

  private Models() {}

  @SuppressWarnings("unchecked") // SerializableModelFactory works for any T extending Serializable
  public static <T extends Serializable>
      SerializableFunction2<T, Model<T>> serializableModelFactory() {
    return (SerializableFunction2<T, Model<T>>) (Object) SerializableModelFactory.INSTANCE;
  }

  @SuppressWarnings({
    "rawtypes",
    "unchecked"
  }) // SerializableModelFactory works for any T extending Serializable
  private enum SerializableModelFactory implements SerializableFunction2<Serializable, Model> {
    INSTANCE;

    @Override
    public Model apply(Serializable input) {
      return new Model(input);
    }
  }

  @SuppressWarnings("unchecked") // ModelGetObjectFunction works for any T
  public static <T> SerializableFunction2<? super IModel<? extends T>, T> getObject() {
    return (SerializableFunction2<? super IModel<? extends T>, T>) ModelGetObjectFunction.INSTANCE;
  }

  public static <T> T getObject(IModel<? extends T> model) {
    return Models.<T>getObject().apply(model);
  }

  @SuppressWarnings("rawtypes") // ModelGetObjectFunction works for any T
  private enum ModelGetObjectFunction implements SerializableFunction2<IModel, Object> {
    INSTANCE;

    @Override
    public Object apply(IModel input) {
      return input == null ? null : input.getObject();
    }
  }

  /**
   * A "placeholder" model, for use when the actual behavior of getObject() and setObject() do not
   * matter.
   *
   * <p>{@link #getObject()} always returns null and {@link #setObject(Object)} and {@link
   * #detach()} do nothing.
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
   * A static model of <code>Map<String, Object></code>. Useful as a data model for {@link
   * StringResourceModel}.
   */
  public static MapModelBuilder<String, Object> dataMap() {
    return new MapModelBuilder<>();
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
   *
   * <p>Useful when calling
   */
  public static <T> IModel<T> transientModel(T value) {
    return new TransientModel<>(value);
  }

  private static class TransientModel<T> implements IModel<T> {

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

  /**
   * @param model The model to be wrapped.
   * @param component The component to be used as a wrapping parameter.
   * @return The wrapped model, or the original model if it does not implement {@link
   *     IComponentAssignedModel}.
   */
  public static <T> IModel<T> wrap(IModel<T> model, Component component) {
    if (model instanceof IComponentAssignedModel) {
      return ((IComponentAssignedModel<T>) model).wrapOnAssignment(component);
    } else {
      return model;
    }
  }

  public static <T, C extends Collection<T>, M extends IModel<T>>
      IItemModelAwareCollectionModel<T, C, M> filterByModel(
          IItemModelAwareCollectionModel<T, ? extends Collection<T>, M> unfiltered,
          SerializablePredicate2<M> modelPredicate,
          SerializableSupplier2<? extends C> collectionSupplier) {
    return new FilterByModelItemModelAwareCollectionModel<>(
        unfiltered, modelPredicate, collectionSupplier);
  }

  /**
   * A utility method that provides a sensible default implementation of {@link
   * IItemModelAwareCollectionModel#iterator()}.
   *
   * <p>In particular, this implementation defers the call to {@code iterator} on the underlying
   * {@link Iterable}. This is necessary because of how RefreshingView works: it first gets the
   * iterator and *then* detaches its items, which may indirectly detach the {@link
   * ICollectionModel} and thus trigger changes to the underlying {@link Iterable}.
   *
   * @param iterable The model iterable.
   * @return The model iterator.
   */
  public static <T> Iterator<T> collectionModelIterator(Iterable<T> iterable) {
    Iterator<T> iterator = Iterators2.deferred(iterable);
    return Iterators.unmodifiableIterator(iterator);
  }

  /**
   * A utility method that provides a sensible default implementation of {@link
   * ICollectionModel#iterator(long, long)}.
   *
   * <p>In particular, this implementation defers the call to {@code iterator} on the underlying
   * {@link Iterable}. This is necessary because of how RefreshingView works: it first gets the
   * iterator and *then* detaches its items, which may indirectly detach the {@link
   * ICollectionModel} and thus trigger changes to the underlying {@link Iterable}.
   *
   * @param iterable The model iterable.
   * @param offset The offset the returned iterator should start from.
   * @param limit The maximum number of items the returned iterator should provide.
   * @return The model iterator.
   */
  public static <T> Iterator<T> collectionModelIterator(
      Iterable<T> iterable, long offset, long limit) {
    Iterable<T> offsetedIterable = Iterables.skip(iterable, Ints.saturatedCast(offset));
    return Iterators.limit(collectionModelIterator(offsetedIterable), Ints.saturatedCast(limit));
  }

  /**
   * A utility method that provides a sensible default implementation for {@link
   * IMapModel#valueModel(IModel)}.
   */
  public static <K, V> IModel<V> mapModelValueModel(
      final IMapModel<K, V, ?> mapModel, final IModel<? extends K> keyModel) {
    return new MapModelValueModel<>(mapModel, keyModel);
  }

  private static final class MapModelValueModel<K, V> implements IModel<V> {
    private static final long serialVersionUID = 1L;

    private final IMapModel<K, V, ?> mapModel;
    private final IModel<? extends K> keyModel;

    public MapModelValueModel(IMapModel<K, V, ?> mapModel, IModel<? extends K> keyModel) {
      this.mapModel = mapModel;
      this.keyModel = keyModel;
    }

    @Override
    public void detach() {
      mapModel.detach();
      keyModel.detach();
    }

    @Override
    public V getObject() {
      Map<K, V> map = mapModel.getObject();
      return map == null ? null : map.get(keyModel.getObject());
    }

    @Override
    public void setObject(V object) {
      mapModel.put(keyModel.getObject(), object);
    }

    @Override
    public boolean equals(Object obj) {
      if (!(obj instanceof MapModelValueModel<?, ?>)) {
        return false;
      }

      MapModelValueModel<?, ?> other = (MapModelValueModel<?, ?>) obj;

      return new EqualsBuilder()
          .append(mapModel, other.mapModel)
          .append(keyModel, other.keyModel)
          .build();
    }

    @Override
    public int hashCode() {
      return new HashCodeBuilder().append(mapModel).append(keyModel).build();
    }
  }
}
