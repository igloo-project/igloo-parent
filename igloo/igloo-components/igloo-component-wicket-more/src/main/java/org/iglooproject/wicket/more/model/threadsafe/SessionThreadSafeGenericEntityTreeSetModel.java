package org.iglooproject.wicket.more.model.threadsafe;

import java.io.Serializable;
import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;
import org.iglooproject.functional.SerializableSupplier2;
import org.iglooproject.functional.Suppliers2;
import org.iglooproject.jpa.business.generic.model.GenericEntity;

public class SessionThreadSafeGenericEntityTreeSetModel<
        K extends Serializable & Comparable<K>, E extends GenericEntity<K, ?>>
    extends AbstractSessionThreadSafeGenericEntityCollectionModel<K, E, SortedSet<E>> {

  private static final long serialVersionUID = -3618904709308977111L;

  public static <K extends Serializable & Comparable<K>, E extends GenericEntity<K, ?>>
      SessionThreadSafeGenericEntityTreeSetModel<K, E> of(Class<E> clazz) {
    return new SessionThreadSafeGenericEntityTreeSetModel<>(clazz, Suppliers2.<E>treeSet());
  }

  public static <K extends Serializable & Comparable<K>, E extends GenericEntity<K, ?>>
      SessionThreadSafeGenericEntityTreeSetModel<K, E> of(
          Class<E> clazz, Comparator<? super E> comparator) {
    return new SessionThreadSafeGenericEntityTreeSetModel<>(
        clazz, Suppliers2.<E>treeSet(comparator));
  }

  public SessionThreadSafeGenericEntityTreeSetModel(
      Class<E> clazz, SerializableSupplier2<? extends TreeSet<E>> newCollectionSupplier) {
    super(clazz, newCollectionSupplier);
  }
}
