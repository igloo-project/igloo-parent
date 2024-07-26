package org.iglooproject.wicket.more.jqplot.data.provider;

import com.google.common.collect.Table;
import java.util.Collection;
import org.apache.wicket.model.IModel;

public class JQPlotTableDataProvider<S, K, V> extends AbstractJQPlotDataProvider<S, K, V> {

  private static final long serialVersionUID = 7354794798034484146L;

  private final IModel<Table<S, K, V>> tableModel;

  public JQPlotTableDataProvider(IModel<Table<S, K, V>> tableModel) {
    super();
    this.tableModel = tableModel;
  }

  private Table<S, K, V> getTable() {
    return tableModel.getObject();
  }

  @Override
  public V getValue(S serie, K key) {
    return getTable().get(serie, key);
  }

  @Override
  public Collection<S> getSeries() {
    return getTable().rowKeySet();
  }

  @Override
  public Collection<K> getKeys() {
    return getTable().columnKeySet();
  }

  @Override
  public void detach() {
    tableModel.detach();
  }

  @Override
  public Collection<V> getValues() {
    return getTable().values();
  }
}
