package org.iglooproject.jpa.more.business.difference.access;

import com.google.common.collect.Multimap;
import de.danielbechler.diff.access.Accessor;
import de.danielbechler.diff.selector.ElementSelector;
import java.util.Map;
import org.iglooproject.jpa.more.business.difference.selector.MapValueByKeySelector;
import org.jspecify.annotations.Nullable;

public class MultimapValueByKeyAccessor<K> implements Accessor, IKeyAwareAccessor<K> {

  private final K key;

  public MultimapValueByKeyAccessor(K key) {
    super();
    this.key = key;
  }

  @Override
  public K getKey() {
    return key;
  }

  @Override
  public ElementSelector getElementSelector() {
    return new MapValueByKeySelector<>(key);
  }

  @SuppressWarnings("rawtypes")
  protected @Nullable Map objectToMap(@Nullable Object object) {
    if (object == null) {
      return null;
    } else if (object instanceof Multimap) {
      return ((Multimap) object).asMap();
    } else {
      throw new IllegalArgumentException("Expecting a Multimap");
    }
  }

  @Override
  public @Nullable Object get(final @Nullable Object target) {
    final Map<?, ?> targetMap = objectToMap(target);
    if (targetMap != null) {
      return targetMap.get(key);
    }
    return null;
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  @Override
  public void set(final @Nullable Object target, final @Nullable Object value) {
    final Map targetMap = objectToMap(target);
    if (targetMap != null) {
      targetMap.put(key, value);
    }
  }

  @Override
  public void unset(final @Nullable Object target) {
    final Map<?, ?> targetMap = objectToMap(target);
    if (targetMap != null) {
      targetMap.remove(key);
    }
  }
}
