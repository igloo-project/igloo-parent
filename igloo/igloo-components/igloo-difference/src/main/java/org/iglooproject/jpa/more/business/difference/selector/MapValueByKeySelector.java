package org.iglooproject.jpa.more.business.difference.selector;

import de.danielbechler.diff.selector.ElementSelector;
import de.danielbechler.util.Strings;
import java.util.Objects;

public class MapValueByKeySelector<K> extends ElementSelector implements IKeyAwareSelector<K> {

  private final K key;

  public MapValueByKeySelector(K key) {
    this.key = key;
  }

  @Override
  public K getKey() {
    return key;
  }

  @Override
  public String toHumanReadableString() {
    return Strings.toSingleLineString(key);
  }

  @Override
  @SuppressWarnings("unchecked")
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof MapValueByKeySelector<?> other)) {
      return false;
    }
    return Objects.equals(key, (K) other.key);
  }

  @Override
  public int hashCode() {
    return Objects.hash(key);
  }
}
