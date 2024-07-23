package org.iglooproject.jpa.more.business.difference.selector;

import com.google.common.base.Equivalence;
import de.danielbechler.diff.selector.ElementSelector;
import de.danielbechler.util.Strings;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class MultimapEntrySelector<K, V> extends ElementSelector implements IKeyAwareSelector<K> {

  private final Map.Entry<K, V> entry;

  private final Equivalence<? super Entry<K, V>> entryEquivalence;

  public MultimapEntrySelector(
      Map.Entry<K, V> entry, Equivalence<? super Entry<K, V>> entryEquivalence) {
    this.entry = entry;
    this.entryEquivalence = entryEquivalence;
  }

  @Override
  @SuppressWarnings("unchecked")
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (o instanceof MultimapEntrySelector) {
      MultimapEntrySelector<?, ?> other = (MultimapEntrySelector<?, ?>) o;
      return entryEquivalence.equals(other.entryEquivalence)
          && entryEquivalence.equivalent(entry, (Entry<K, V>) other.entry);
    }

    return false;
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder().append(entryEquivalence.hash(entry)).build();
  }

  @Override
  public String toHumanReadableString() {
    return Strings.toSingleLineString(entry);
  }

  @Override
  public K getKey() {
    return entry.getKey();
  }
}
