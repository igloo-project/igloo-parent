package org.iglooproject.jpa.more.business.difference.selector;

import com.google.common.base.Equivalence;
import de.danielbechler.diff.selector.ElementSelector;
import de.danielbechler.util.Strings;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

public class MultimapEntrySelector<K, V> extends ElementSelector implements IKeyAwareSelector<K> {

  private final Map.Entry<K, V> entry;

  private final Equivalence<? super Entry<K, V>> entryEquivalence;

  public MultimapEntrySelector(
      Map.Entry<K, V> entry, Equivalence<? super Entry<K, V>> entryEquivalence) {
    this.entry = entry;
    this.entryEquivalence = entryEquivalence;
  }

  @Override
  public K getKey() {
    return entry.getKey();
  }

  @Override
  public String toHumanReadableString() {
    return Strings.toSingleLineString(entry);
  }

  @Override
  @SuppressWarnings("unchecked")
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof MultimapEntrySelector<?, ?> other)) {
      return false;
    }
    return Objects.equals(entryEquivalence, other.entryEquivalence)
        && entryEquivalence.equivalent(entry, (Entry<K, V>) other.entry);
  }

  @Override
  public int hashCode() {
    return Objects.hash(entryEquivalence, entryEquivalence.hash(entry));
  }
}
