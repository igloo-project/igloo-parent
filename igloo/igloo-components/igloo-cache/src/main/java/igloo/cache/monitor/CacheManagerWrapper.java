package igloo.cache.monitor;

import java.util.Objects;
import org.springframework.cache.CacheManager;

/**
 * Object adding name to {@link CacheManager} contract. <code>name</code> is the spring context's
 * bean bean.
 */
public class CacheManagerWrapper {

  private final String name;
  private final CacheManager cacheManager;

  public CacheManagerWrapper(String name, CacheManager cacheManager) {
    this.name = name;
    this.cacheManager = cacheManager;
  }

  public String getName() {
    return name;
  }

  public CacheManager getCacheManager() {
    return cacheManager;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof CacheManagerWrapper other)) {
      return false;
    }
    return Objects.equals(cacheManager, other.cacheManager);
  }

  @Override
  public int hashCode() {
    return Objects.hash(cacheManager);
  }

  public void clearAll() {
    cacheManager.getCacheNames().forEach(n -> cacheManager.getCache(n).clear());
  }
}
