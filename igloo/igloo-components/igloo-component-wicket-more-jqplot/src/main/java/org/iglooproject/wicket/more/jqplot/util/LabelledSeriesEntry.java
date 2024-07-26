package org.iglooproject.wicket.more.jqplot.util;

import java.util.Arrays;
import java.util.List;
import nl.topicus.wqplot.data.AbstractSeriesEntry;

public class LabelledSeriesEntry<K, V> extends AbstractSeriesEntry<K, V> {

  private final String label;

  public LabelledSeriesEntry(K key, V value, String label) {
    super(key, value);
    this.label = label;
  }

  public String getLabel() {
    return label;
  }

  @Override
  public List<Object> values() {
    return Arrays.asList(getKey(), getValue(), getLabel());
  }
}
