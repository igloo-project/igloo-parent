package org.iglooproject.wicket.more.jqplot.data.provider;

import com.google.common.base.Objects;
import com.google.common.collect.Sets;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import org.apache.wicket.model.IModel;

public class JQPlotMapDataProvider<S, K, V> extends AbstractJQPlotDataProvider<S, K, V> {

  private static final long serialVersionUID = 7354794798034484146L;

  private static final Object SERIES = null;

  private static final Collection<Object> SERIES_COLLECTION =
      Collections.unmodifiableSet(Sets.newHashSet(SERIES));

  private final IModel<Map<K, V>> mapModel;

  public JQPlotMapDataProvider(IModel<Map<K, V>> rawDataModel) {
    super();
    this.mapModel = rawDataModel;
  }

  private Map<K, V> getMap() {
    return mapModel.getObject();
  }

  @Override
  public V getValue(S serie, K key) {
    return Objects.equal(serie, SERIES) ? getMap().get(key) : null;
  }

  @Override
  @SuppressWarnings("unchecked")
  public Collection<S> getSeries() {
    return (Collection<S>) SERIES_COLLECTION;
  }

  @Override
  public Collection<K> getKeys() {
    return getMap().keySet();
  }

  @Override
  public void detach() {
    mapModel.detach();
  }
}
