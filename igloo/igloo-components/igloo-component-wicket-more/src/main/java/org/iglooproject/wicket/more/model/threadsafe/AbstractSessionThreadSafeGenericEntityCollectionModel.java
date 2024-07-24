package org.iglooproject.wicket.more.model.threadsafe;

import com.google.common.collect.Lists;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.iglooproject.functional.SerializableSupplier2;
import org.iglooproject.jpa.business.generic.model.GenericEntity;
import org.iglooproject.jpa.business.generic.service.IEntityService;
import org.iglooproject.jpa.util.HibernateUtils;

public abstract class AbstractSessionThreadSafeGenericEntityCollectionModel<
        K extends Serializable & Comparable<K>,
        E extends GenericEntity<K, ?>,
        C extends Collection<E>>
    extends SessionThreadSafeDerivedSerializableStateLoadableDetachableModel<
        C, AbstractSessionThreadSafeGenericEntityCollectionModel<K, E, C>.SerializableState> {

  private static final long serialVersionUID = -1768835911782930879L;

  @SpringBean private IEntityService entityService;

  private final Class<E> clazz;

  private final SerializableSupplier2<? extends C> newCollectionSupplier;

  protected AbstractSessionThreadSafeGenericEntityCollectionModel(
      Class<E> clazz, SerializableSupplier2<? extends C> newCollectionSupplier) {
    super();
    Injector.get().inject(this);

    this.clazz = clazz;
    this.newCollectionSupplier = newCollectionSupplier;
    setObject(null); // Sets to an empty collection
  }

  protected C createEntityCollection() {
    return newCollectionSupplier.get();
  }

  protected K toId(E entity) {
    return entity.getId();
  }

  protected E toEntity(K id) {
    return entityService.getEntity(clazz, id);
  }

  @Override
  protected C load(SerializableState serializableState) {
    C entityCollection = createEntityCollection();

    // Never, ever return null
    if (serializableState == null) {
      return entityCollection;
    }

    List<K> idList = serializableState.idList;
    List<E> unsavedEntityList = serializableState.unsavedEntityList;

    for (int i = 0, unsavedEntityIndex = 0; i < serializableState.idList.size(); ++i) {
      K id = idList.get(i);

      if (id != null) {
        entityCollection.add(toEntity(id));
      } else {
        assert unsavedEntityList.size() > unsavedEntityIndex;
        E unsavedEntity = unsavedEntityList.get(unsavedEntityIndex);
        assert unsavedEntity != null;
        entityCollection.add(unsavedEntity);
        ++unsavedEntityIndex;
      }
    }

    return entityCollection;
  }

  @Override
  protected C wrap(C object) {
    C entityCollection = createEntityCollection();

    // Never, ever return null
    if (object == null) {
      return entityCollection;
    }

    entityCollection.addAll(object);

    return entityCollection;
  }

  @Override
  protected SerializableState makeSerializable(C currentObject) {
    return new SerializableState(currentObject);
  }

  @Override
  protected SerializableState normalizeDetached(SerializableState current) {
    return current == null ? null : current.normalize();
  }

  /** Immutable, serializable state for detached models. */
  protected final class SerializableState implements Serializable {
    private static final long serialVersionUID = 1L;

    private final List<K> idList = Lists.newArrayList();
    private final List<E> unsavedEntityList = Lists.newArrayList();

    private SerializableState() {
      // For clone() only.
    }

    public SerializableState(Collection<? extends E> entityCollection) {
      if (entityCollection != null) {
        // On sauvegarde les éventuelles modifications apportées à entityCollection
        idList.clear();
        unsavedEntityList.clear();

        for (E entity : entityCollection) {
          if (entity.isNew()) {
            unsavedEntityList.add(HibernateUtils.unwrap(entity));
            idList.add(null);
          } else {
            // Do nothing with unsavedEntityList here (on purpose)
            idList.add(toId(entity));
          }
        }
      }
    }

    public SerializableState normalize() {
      for (E unsavedEntity : unsavedEntityList) {
        if (!unsavedEntity.isNew()) {
          // Abnormal state => normalize
          return doNormalize();
        }
      }
      return this;
    }

    private SerializableState doNormalize() {
      SerializableState normalized = copy();
      for (int i = 0, unsavedEntityIndex = 0; i < normalized.idList.size(); ++i) {
        K id = normalized.idList.get(i);

        if (id == null) {
          assert normalized.unsavedEntityList.size() > unsavedEntityIndex;
          E unsavedEntity = normalized.unsavedEntityList.get(unsavedEntityIndex);
          assert unsavedEntity != null;
          if (unsavedEntity.isNew()) {
            // Do nothing
            ++unsavedEntityIndex;
          } else {
            // Fix serializable data
            normalized.idList.set(i, toId(unsavedEntity));
            normalized.unsavedEntityList.remove(unsavedEntityIndex);
          }
        }
      }
      return normalized;
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean equals(Object obj) {
      if (obj == null) {
        return false;
      }
      if (obj == this) {
        return true;
      }
      if (!(obj
          instanceof AbstractSessionThreadSafeGenericEntityCollectionModel.SerializableState)) {
        return false;
      }
      SerializableState other = (SerializableState) obj;

      return new EqualsBuilder()
          .append(idList, other.idList)
          .append(unsavedEntityList, other.unsavedEntityList)
          .build();
    }

    @Override
    public int hashCode() {
      return new HashCodeBuilder().append(idList).append(unsavedEntityList).toHashCode();
    }

    public SerializableState copy() {
      // Can't use super.clone() since idList and co. are final.
      SerializableState clone = new SerializableState();
      clone.idList.addAll(idList);
      clone.unsavedEntityList.addAll(unsavedEntityList);
      return clone;
    }
  }
}
