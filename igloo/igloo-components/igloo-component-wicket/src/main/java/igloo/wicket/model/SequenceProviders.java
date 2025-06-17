package igloo.wicket.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

public final class SequenceProviders {

  private SequenceProviders() {}

  /**
   * @return A read-only model representing a list of each model object, in the same order.
   */
  public static <T, M extends IModel<T>>
      IItemModelAwareCollectionModel<T, List<T>, M> fromItemModels(Collection<? extends M> models) {
    return new ConstantCollectionModel<>(models);
  }

  public static class ConstantCollectionModel<T, M extends IModel<T>>
      extends LoadableDetachableModel<List<T>>
      implements IItemModelAwareCollectionModel<T, List<T>, M> {
    private static final long serialVersionUID = 1L;

    private final Collection<M> models;

    public ConstantCollectionModel(Collection<? extends M> models) {
      super();
      this.models = List.copyOf(models);
    }

    @Override
    public void detach() {
      Detachables.detach(models);
    }

    @Override
    public Iterator<M> iterator(long offset, long limit) {
      return Models.collectionModelIterator(models, offset, limit);
    }

    @Override
    public long size() {
      return models.size();
    }

    @Override
    protected List<T> load() {
      List<T> result = new ArrayList<>(models.size());
      for (M model : models) {
        result.add(model.getObject());
      }
      return Collections.unmodifiableList(result);
    }

    @Override
    public void setObject(List<T> object) {
      throw new UnsupportedOperationException("This model is read-only");
    }

    @Override
    public Iterator<M> iterator() {
      return Models.collectionModelIterator(models);
    }

    @Override
    public void add(T item) {
      throw new UnsupportedOperationException("This model is read-only");
    }

    @Override
    public void remove(T item) {
      throw new UnsupportedOperationException("This model is read-only");
    }

    @Override
    public void update(List<T> collection) {
      throw new UnsupportedOperationException("This model is read-only");
    }

    @Override
    public void clear() {
      throw new UnsupportedOperationException("This model is read-only");
    }
  }

  public static <T> ISequenceProvider<T> forDataProvider(IDataProvider<T> dataProvider) {
    return new DataProviderSequenceProviderAdapter<>(dataProvider);
  }

  public static class DataProviderSequenceProviderAdapter<T> implements ISequenceProvider<T> {
    private static final long serialVersionUID = 1L;

    private final IDataProvider<T> dataProvider;

    public DataProviderSequenceProviderAdapter(IDataProvider<T> dataProvider) {
      this.dataProvider = dataProvider;
    }

    @Override
    public void detach() {
      Detachables.detach(dataProvider);
    }

    @Override
    public Iterator<? extends IModel<T>> iterator(final long offset, final long limit) {
      return new Iterator<IModel<T>>() {
        private Iterator<? extends T> dataIterator = dataProvider.iterator(offset, limit);

        @Override
        public boolean hasNext() {
          return dataIterator.hasNext();
        }

        @Override
        public IModel<T> next() {
          return dataProvider.model(dataIterator.next());
        }

        @Override
        public void remove() {
          dataIterator.remove();
        }
      };
    }

    @Override
    public long size() {
      return dataProvider.size();
    }
  }
}
