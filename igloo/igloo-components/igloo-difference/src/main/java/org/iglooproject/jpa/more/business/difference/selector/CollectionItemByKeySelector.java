package org.iglooproject.jpa.more.business.difference.selector;

import com.google.common.base.Equivalence;
import de.danielbechler.diff.selector.ElementSelector;
import de.danielbechler.util.Strings;
import java.util.Objects;
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
  public K getKey() {
    return key;
  }

  @Override
  public String toHumanReadableString() {
    return humanReadableString;
  }

  @Override
  @SuppressWarnings("unchecked")
  public boolean equals(final Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof CollectionItemByKeySelector<?, ?> other)) {
      return false;
    }
    return Objects.equals(this.keyFunction, other.keyFunction)
        && Objects.equals(this.equivalence, other.equivalence)
        && this.equivalence.equivalent(this.key, (K) other.key);
  }

  @Override
  public int hashCode() {
    return Objects.hash(keyFunction, equivalence, equivalence.hash(key));
  }
}
