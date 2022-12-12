package igloo.cache.spring;

import java.util.List;

import igloo.cache.monitor.CacheManagerWrapper;

/**
 * @see CacheManagerRegistry
 */
public interface ICacheManagerRegistry {

	List<CacheManagerWrapper> getCacheManagers();

}