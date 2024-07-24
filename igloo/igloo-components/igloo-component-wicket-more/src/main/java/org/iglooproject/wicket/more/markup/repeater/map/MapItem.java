package org.iglooproject.wicket.more.markup.repeater.map;

import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;

public class MapItem<K, V> extends Item<K> {
  private static final long serialVersionUID = 1L;

  private final IModel<V> valueModel;

  public MapItem(String id, int index, IModel<K> keyModel, IModel<V> valueModel) {
    super(id, index, keyModel);
    this.valueModel = valueModel;
  }

  public IModel<V> getValueModel() {
    return valueModel;
  }
}
