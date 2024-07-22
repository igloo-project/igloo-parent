package org.iglooproject.wicket.more.markup.repeater.collection;

import igloo.wicket.model.ICollectionModel;
import java.util.Collection;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.iglooproject.functional.SerializableFunction2;
import org.iglooproject.wicket.more.markup.repeater.sequence.SequenceView;
import org.iglooproject.wicket.more.model.ReadOnlyCollectionModel;

public abstract class CollectionView<T> extends SequenceView<T> {

  private static final long serialVersionUID = 1L;

  public CollectionView(String id, ICollectionModel<T, ?> collectionModel) {
    super(id, collectionModel);
    setDefaultModel(collectionModel);
  }

  /**
   * Creates a {@link ReadOnlyCollectionModel read-only} view on the given model, using the given
   * factory for generating item models.
   */
  @SuppressWarnings("unchecked")
  public CollectionView(
      String id,
      IModel<? extends Collection<T>> collectionModel,
      SerializableFunction2<? super T, ? extends IModel<T>> itemModelFactory) {
    this(
        id,
        collectionModel instanceof ICollectionModel
            ? (ICollectionModel<T, ?>) collectionModel
            : ReadOnlyCollectionModel.of(collectionModel, itemModelFactory));
  }

  @Override
  protected abstract void populateItem(Item<T> item);

  @SuppressWarnings(
      "unchecked") // The sequence provider is known to be a ICollectionModel, see constructor
  public ICollectionModel<T, ?> getModel() {
    return (ICollectionModel<T, ?>) getSequenceProvider();
  }
}
