package igloo.cache.monitor;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Policy.Eviction;

/** Adapt a Spring Caffeine cache instance. */
public class CaffeineCacheWrapper implements ICacheWrapper {

  private final Cache<?, ?> cache;
  private final String name;

  public CaffeineCacheWrapper(String name, Cache<?, ?> cache) {
    super();
    this.cache = cache;
    this.name = name;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public long getCacheHits() {
    return cache.stats().hitCount();
  }

  @Override
  public long getCacheMisses() {
    return cache.stats().missCount();
  }

  @Override
  public long getCacheGets() {
    return cache.stats().requestCount();
  }

  @Override
  public long getCachePuts() {
    return cache.stats().loadCount();
  }

  @Override
  public long getCacheEvictions() {
    return cache.stats().evictionCount();
  }

  @Override
  public long getMaxSize() {
    return cache.policy().eviction().map(Eviction::getMaximum).orElse(0l);
  }

  @Override
  public void setMaxSize(long maxSize) {
    cache.policy().eviction().ifPresent(e -> e.setMaximum(maxSize));
  }

  @Override
  public void clear() {
    cache.invalidateAll();
  }

  @Override
  public Long getCurrentSize() {
    return cache.estimatedSize();
  }
}
