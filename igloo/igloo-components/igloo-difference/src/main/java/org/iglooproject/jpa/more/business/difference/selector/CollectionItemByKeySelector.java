package org.iglooproject.jpa.more.business.difference.selector;

import com.google.common.base.Equivalence;
import de.danielbechler.diff.selector.ElementSelector;
import de.danielbechler.util.Strings;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.iglooproject.functional.Function2;

public class CollectionItemByKeySelector<T, K> extends ElementSelector
    implements IKeyAwareSelector<K> {

  private final Function2<? super T, ? extends K> keyFunction;

  private final Equivalence<? super K> equivalence;

  private final K key;

  private final String humanReadableString;

  public CollectionItemByKeySelector(
      Function2<? super T, ? extends K> keyFunction,
      Equivalence<? super K> equivalence,
      K key,
      String humanReadableString) {
    this.keyFunction = keyFunction;
    this.equivalence = equivalence;
    this.key = key;
    this.humanReadableString = Strings.toSingleLineString(humanReadableString);
  }

  @Override
  @SuppressWarnings("unchecked")
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (o instanceof CollectionItemByKeySelector) {
      CollectionItemByKeySelector<?, ?> other = (CollectionItemByKeySelector<?, ?>) o;
      return keyFunction.equals(other.keyFunction)
          && equivalence.equals(other.equivalence)
          && equivalence.equivalent(key, (K) other.key);
    }

    return false;
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder()
        .append(keyFunction)
        .append(equivalence)
        .append(equivalence.hash(key))
        .build();
  }

  @Override
  public String toHumanReadableString() {
    return humanReadableString;
  }

  @Override
  public K getKey() {
    return key;
  }
}
