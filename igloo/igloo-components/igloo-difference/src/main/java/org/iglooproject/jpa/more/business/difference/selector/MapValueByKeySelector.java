package org.iglooproject.jpa.more.business.difference.selector;

import com.google.common.base.Objects;
import de.danielbechler.diff.selector.ElementSelector;
import de.danielbechler.util.Strings;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class MapValueByKeySelector<K> extends ElementSelector implements IKeyAwareSelector<K> {

  private final K key;

  public MapValueByKeySelector(K key) {
    this.key = key;
  }

  @Override
  @SuppressWarnings("unchecked")
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (o instanceof MapValueByKeySelector) {
      MapValueByKeySelector<?> other = (MapValueByKeySelector<?>) o;
      return Objects.equal(key, (K) other.key);
    }

    return false;
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder().append(key).build();
  }

  @Override
  public String toHumanReadableString() {
    return Strings.toSingleLineString(key);
  }

  @Override
  public K getKey() {
    return key;
  }
}
