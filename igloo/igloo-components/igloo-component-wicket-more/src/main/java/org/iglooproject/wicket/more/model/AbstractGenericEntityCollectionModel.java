package org.iglooproject.wicket.more.model;

import com.google.common.collect.Lists;
import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.iglooproject.functional.SerializableSupplier2;
import org.iglooproject.jpa.business.generic.model.GenericEntity;
import org.iglooproject.jpa.business.generic.service.IEntityService;

public abstract class AbstractGenericEntityCollectionModel<
        K extends Serializable & Comparable<K>,
        E extends GenericEntity<K, ?>,
        C extends Collection<E>>
    extends LoadableDetachableModel<C> {

  private static final long serialVersionUID = -1768835911782930879L;

  @SpringBean private IEntityService entityService;

  private transient boolean attached = false;

  private final List<K> idList = Lists.newArrayList();

  private final List<E> unsavedEntityList = Lists.newArrayList();

  private final Class<E> clazz;

  private final SerializableSupplier2<? extends C> newCollectionSupplier;

  private transient C entityCollection = null;

  protected AbstractGenericEntityCollectionModel(
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
  protected C load() {
    entityCollection = createEntityCollection();

    for (int i = 0, unsavedEntityIndex = 0; i < idList.size(); ++i) {
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

    attached = true;

    return entityCollection;
  }

  /**
   * WARNING: if the client calls <code>setObject(null)</code>, a subsequent call to <code>
   * getObject()</code> will not return <code>null</code>, but <em>an empty collection</em>.
   */
  @Override
  public void setObject(C object) {
    entityCollection = createEntityCollection();
    if (object != null) {
      entityCollection.addAll(object);
    }
    attached = true;
    super.setObject(entityCollection);
  }

  @Override
  public void detach() {
    if (!attached) {
      fixSerializableData();
      return;
    }
    updateSerializableData();
    super.detach();
    attached = false;
  }

  private void updateSerializableData() {
    assert entityCollection != null;

    // Saves the possible modifications applied to entityCollection
    idList.clear();
    unsavedEntityList.clear();

    for (E entity : entityCollection) {
      if (entity.isNew()) {
        unsavedEntityList.add(entity);
        idList.add(null);
      } else {
        // Do nothing with unsavedEntityList here (on purpose)
        K id = toId(entity);
        assert id != null;
        idList.add(id);
      }
    }

    entityCollection.clear();
  }

  /**
   * If an entity has been persisted since this model has been detached, then fix the serializable
   * data (this may happen if two models reference the same non-persisted entity, for instance)
   */
  private void fixSerializableData() {
    for (int i = 0, unsavedEntityIndex = 0; i < idList.size(); ++i) {
      K id = idList.get(i);

      if (id == null) {
        assert unsavedEntityList.size() > unsavedEntityIndex;
        E unsavedEntity = unsavedEntityList.get(unsavedEntityIndex);
        assert unsavedEntity != null;
        if (unsavedEntity.isNew()) {
          // Do nothing
          ++unsavedEntityIndex;
        } else {
          // Fix serializable data
          idList.set(i, toId(unsavedEntity));
          unsavedEntityList.remove(unsavedEntityIndex);
        }
      }
    }
  }

  public List<K> getIdList() {
    return Collections.unmodifiableList(idList);
  }
}
