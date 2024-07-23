package igloo.cache.spring;

import igloo.cache.monitor.CacheManagerWrapper;
import java.util.List;

/**
 * @see CacheManagerRegistry
 */
public interface ICacheManagerRegistry {

  List<CacheManagerWrapper> getCacheManagers();
}
