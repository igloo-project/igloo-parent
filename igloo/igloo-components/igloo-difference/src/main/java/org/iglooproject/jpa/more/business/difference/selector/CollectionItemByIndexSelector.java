package org.iglooproject.jpa.more.business.difference.selector;

import de.danielbechler.diff.selector.ElementSelector;
import java.util.Objects;

public class CollectionItemByIndexSelector extends ElementSelector
    implements IKeyAwareSelector<Integer> {

  private final int index;

  public CollectionItemByIndexSelector(int index) {
    this.index = index;
  }

  @Override
  public Integer getKey() {
    return index;
  }

  @Override
  public String toHumanReadableString() {
    return "[" + index + "]";
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof CollectionItemByIndexSelector other)) {
      return false;
    }
    return Objects.equals(index, other.index);
  }

  @Override
  public int hashCode() {
    return Objects.hash(index);
  }
}
