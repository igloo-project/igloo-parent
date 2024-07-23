package igloo.wicket.model;

import java.util.Collection;
import org.apache.wicket.model.IModel;
import org.danekja.java.util.function.serializable.SerializableSupplier;

/**
 * A wrapper allowing to handle an IModel<? extends Collection<T>> as if it were an
 * IModel<Collection<T>>, both when reading it and writing to it.
 *
 * @param <T> The collection's element type
 * @param <C> The underlying collection type
 */
public class ConcreteCollectionToCollectionWrapperModel<T, C extends Collection<T>>
    implements IModel<Collection<T>> {

  private static final long serialVersionUID = -6342356257002249205L;

  private final IModel<C> concreteCollectionModel;

  private final SerializableSupplier<? extends C> concreteCollectionSupplier;

  public ConcreteCollectionToCollectionWrapperModel(
      IModel<C> concreteCollectionModel,
      SerializableSupplier<? extends C> concreteCollectionSupplier) {
    this.concreteCollectionModel = concreteCollectionModel;
    this.concreteCollectionSupplier = concreteCollectionSupplier;
  }

  @Override
  public Collection<T> getObject() {
    return concreteCollectionModel.getObject();
  }

  @Override
  public void setObject(Collection<T> object) {
    C concreteCollection;
    if (object == null) {
      concreteCollection = null;
    } else {
      concreteCollection = concreteCollectionSupplier.get();
      concreteCollection.addAll(object);
    }
    concreteCollectionModel.setObject(concreteCollection);
  }

  @Override
  public void detach() {
    concreteCollectionModel.detach();
  }
}
