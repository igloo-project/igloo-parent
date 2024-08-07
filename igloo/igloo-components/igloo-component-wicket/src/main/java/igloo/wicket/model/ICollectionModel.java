package igloo.wicket.model;

import java.util.Collection;
import org.apache.wicket.model.IModel;

/**
 * A {@link ISequenceProvider} that also is a model of collection, and that provides some write
 * operations.
 *
 * @author yrodiere
 * @param <T> The element type.
 * @param <C> The collection type.
 */
public interface ICollectionModel<T, C extends Collection<T>>
    extends IModel<C>, ISequenceProvider<T> {

  void add(T item);

  void remove(T item);

  void clear();
}
