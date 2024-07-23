package org.iglooproject.wicket.more.model.optimization;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import org.iglooproject.jpa.business.generic.model.GenericEntity;
import org.iglooproject.wicket.more.markup.repeater.data.LoadableDetachableDataProvider;

public abstract class LoadableDetachableGenericEntityDataProvider<
        K extends Serializable & Comparable<K>, E extends GenericEntity<K, ?>>
    extends LoadableDetachableDataProvider<E> implements IGenericEntityDataProvider<K, E> {

  private static final long serialVersionUID = 996035545779191799L;

  private Long lastIdFirst;

  private Long lastIdCount;

  private List<K> lastIdList;

  protected abstract List<K> loadIdList(long first, long count);

  @Override
  public void detach() {
    super.detach();
    lastIdFirst = null;
    lastIdCount = null;
    lastIdList = null;
  }

  @Override
  public final Iterator<? extends K> idIterator(long first, long count) {
    long realCount = Math.min(count, size() - first);
    if (lastIdList != null
        && Long.valueOf(first).equals(lastIdFirst)
        && Long.valueOf(realCount).equals(lastIdCount)) {
      return lastIdList.iterator();
    } else {
      lastIdFirst = first;
      lastIdCount = realCount;
      lastIdList = loadIdList(first, count);
      return lastIdList.iterator();
    }
  }
}
