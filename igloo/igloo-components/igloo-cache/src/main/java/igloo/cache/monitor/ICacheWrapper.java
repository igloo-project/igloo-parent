package igloo.cache.monitor;

import org.bindgen.Bindable;
import org.springframework.cache.Cache;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.jcache.JCacheCache;

/**
 * Provide a common interface for Spring {@link Cache} to allow monitoring:
 *
 * <ul>
 *   <li>Add a <code>getName()</code> method;
 *   <li>Provide statistics method (hits, puts, evictions, current size);
 *   <li>Provide getter and setter to change runtime-value for maximum size.
 * </ul>
 *
 * <p>You may use {@link ICacheWrapper#wrap(Cache)} to instantiate a concrete implementation.
 * Allowed backends are:
 *
 * <ul>
 *   <li>Caffeine
 *   <li>JCache + Caffeine
 * </ul>
 */
@Bindable
public interface ICacheWrapper {

  static ICacheWrapper wrap(Cache cache) {
    if (cache instanceof JCacheCache) {
      // caffeine jcache cache
      return new igloo.cache.monitor.CaffeineCacheWrapper(
          ((JCacheCache) cache).getName(),
          ((JCacheCache) cache)
              .getNativeCache()
              .unwrap(com.github.benmanes.caffeine.cache.Cache.class));
    } else if (cache instanceof CaffeineCache) {
      // caffeine cache
      return new igloo.cache.monitor.CaffeineCacheWrapper(
          cache.getName(), ((CaffeineCache) cache).getNativeCache());
    } else {
      throw new IllegalStateException(
          String.format("Not supported type %s", cache.getClass().getName()));
    }
  }

  void clear();

  String getName();

  long getCacheHits();

  long getCacheMisses();

  long getCachePuts();

  long getCacheEvictions();

  long getMaxSize();

  void setMaxSize(long maxSize);

  Long getCurrentSize();

  default long getCacheGets() {
    return getCacheHits() + getCacheMisses();
  }

  default float getCacheHitRatio() {
    if (getCacheHits() + getCacheMisses() == 0) {
      return 0;
    }

    return getCacheHits() / (getCacheHits() + getCacheMisses() + 0F);
  }

  default float getCacheMissRatio() {
    if (getCacheHits() + getCacheMisses() == 0) {
      return 0;
    }

    return getCacheMisses() / (getCacheHits() + getCacheMisses() + 0F);
  }

  default Float getCacheFillRatio() {
    if (getCurrentSize() == null || Long.valueOf(0).equals(getMaxSize())) {
      // corner cases
      return null;
    }
    return getCurrentSize() / (getMaxSize() + 0F);
  }
}
