package org.iglooproject.wicket.more.jqplot.data.provider;

import java.util.Collection;
import org.apache.wicket.model.IDetachable;
import org.iglooproject.wicket.more.jqplot.data.adapter.IJQPlotDataAdapter;

/**
 * An interface for objects that provide access to JQPlot raw data.
 *
 * <p>Those data will be converted to a JQPlot format using a {@link IJQPlotDataAdapter}.
 *
 * @see IJQPlotDataAdapter
 * @param <S> The type for series
 * @param <K> The type for keys (generally the X axis)
 * @param <V> The type for values (generally the Y axis)
 */
public interface IJQPlotDataProvider<S, K, V> extends IDetachable {

  /**
   * Get the value for the given series and key.
   *
   * @param series
   * @param key
   * @return The value, or null if there is no data for this series and key.
   */
  V getValue(S series, K key);

  /**
   * @return All the series referenced in the data.
   */
  Collection<S> getSeries();

  /**
   * @return All the keys referenced in the data (regardless of the series).
   */
  Collection<K> getKeys();

  /**
   * @return All the keys referenced in the data (regardless of the series or key).
   */
  Collection<V> getValues();
}
