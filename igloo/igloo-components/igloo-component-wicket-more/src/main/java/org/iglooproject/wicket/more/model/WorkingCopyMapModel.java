package org.iglooproject.wicket.more.model;

import igloo.wicket.model.IItemModelAwareCollectionModel;
import igloo.wicket.model.IItemModelAwareMapModel;
import igloo.wicket.model.MapCopyModel;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.apache.wicket.model.IModel;

public class WorkingCopyMapModel<
        K, V, M extends Map<K, V>, MK extends IModel<K>, MV extends IModel<V>>
    extends WorkingCopyModel<M> implements IItemModelAwareMapModel<K, V, M, MK, MV> {

  private static final long serialVersionUID = -4049247716740595168L;

  private final IItemModelAwareMapModel<K, V, M, MK, MV> mapWorkingCopy;

  public WorkingCopyMapModel(IModel<M> reference, MapCopyModel<K, V, M, MK, MV> workingCopy) {
    super(reference, workingCopy);
    this.mapWorkingCopy = workingCopy;
  }

  @Override
  public M getObject() {
    return mapWorkingCopy.getObject();
  }

  @Override
  public void setObject(M object) {
    mapWorkingCopy.setObject(object);
  }

  @Override
  public Iterator<MK> iterator() {
    return mapWorkingCopy.iterator();
  }

  @Override
  public Iterator<MK> iterator(long offset, long limit) {
    return mapWorkingCopy.iterator(offset, limit);
  }

  @Override
  public long size() {
    return mapWorkingCopy.size();
  }

  @Override
  public IItemModelAwareCollectionModel<K, Set<K>, MK> keysModel() {
    return mapWorkingCopy.keysModel();
  }

  @Override
  public IItemModelAwareCollectionModel<V, Collection<V>, MV> valuesModel() {
    return mapWorkingCopy.valuesModel();
  }

  @Override
  public IModel<V> valueModel(IModel<? extends K> keyModel) {
    return mapWorkingCopy.valueModel(keyModel);
  }

  @Override
  public MV valueModelForProvidedKeyModel(IModel<K> keyModel) {
    return mapWorkingCopy.valueModelForProvidedKeyModel(keyModel);
  }

  @Override
  public void put(K key, V value) {
    mapWorkingCopy.put(key, value);
  }

  @Override
  public void remove(K key) {
    mapWorkingCopy.remove(key);
  }

  @Override
  public void clear() {
    mapWorkingCopy.clear();
  }
}
