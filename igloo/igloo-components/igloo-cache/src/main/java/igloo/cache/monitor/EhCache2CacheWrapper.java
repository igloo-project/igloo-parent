package igloo.cache.monitor;

import org.springframework.cache.ehcache.EhCacheCache;

import net.sf.ehcache.Ehcache;
import net.sf.ehcache.statistics.StatisticsGateway;

/**
 * Adapt a Spring ehcache2 cache instance.
 *
 */
public class EhCache2CacheWrapper implements ICacheWrapper {

	private final Ehcache cache;
	private final StatisticsGateway cacheStatistics;

	public EhCache2CacheWrapper(EhCacheCache cache) {
		this.cache = cache.getNativeCache();
		this.cacheStatistics = this.cache.getStatistics(); 
	}

	@Override
	public String getName() {
		return cache.getName();
	}

	@Override
	public void clear() {
		cache.removeAll();
	}

	@Override
	public long getCacheHits() {
		return cacheStatistics.cacheHitCount();
	}

	@Override
	public long getCacheMisses() {
		return cacheStatistics.cacheMissCount();
	}

	@Override
	public long getCachePuts() {
		return cacheStatistics.cachePutCount();
	}

	@Override
	public long getCacheEvictions() {
		return cacheStatistics.cacheEvictedCount();
	}

	@Override
	public long getMaxSize() {
		return cache.getCacheConfiguration().getMaxEntriesLocalHeap();
	}

	@Override
	public void setMaxSize(long maxSize) {
		cache.getCacheConfiguration().setMaxEntriesLocalHeap(maxSize);
	}

	@Override
	public Long getCurrentSize() {
		return cacheStatistics.getLocalHeapSize();
	}
}
