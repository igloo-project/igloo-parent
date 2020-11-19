package org.iglooproject.wicket.more.console.maintenance.ehcache.model;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Status;

import org.bindgen.Bindable;

@Bindable
public class EhCacheCacheInformation {

	private Cache cache;

	public EhCacheCacheInformation(Cache cache) {
		this.cache = cache;
	}

	public String getName() {
		return cache.getName();
	}

	public CacheStatus getStatus() {
		return CacheStatus.from(cache.getStatus());
	}

	public long getMemoryStoreObjectCount() {
		return cache.getStatistics().getLocalHeapSize();
	}

	public long getMaxElementsInMemory() {
		return cache.getCacheConfiguration().getMaxEntriesLocalHeap();
	}

	public void setMaxElementsInMemory(long maxElementsInMemory) {
		cache.getCacheConfiguration().setMaxEntriesLocalHeap(maxElementsInMemory);
	}

	public Float getCacheFillRatio() {
		if (getMaxElementsInMemory() + getMemoryStoreObjectCount() == 0) {
			return null;
		}
		return getMemoryStoreObjectCount() / (getMaxElementsInMemory() + 0F);
	}

	public long getCacheHits() {
		return cache.getStatistics().cacheHitCount();
	}

	public long getCacheMisses() {
		return cache.getStatistics().cacheMissCount();
	}

	public Float getHitRatio() {
		if (getCacheHits() + getCacheMisses() == 0) {
			return null;
		}
		
		return getCacheHits() / (getCacheHits() + getCacheMisses() + 0F);
	}

	public long getEvictionCount() {
		return cache.getStatistics().cacheEvictedCount();
	}

	public Cache getCache() {
		return cache;
	}

	public enum CacheStatus {
		UNINITIALISED,
		ALIVE,
		SHUTDOWN,
		UNKNOWN;
		
		public static CacheStatus from(Status status) {
			if (Status.STATUS_ALIVE.equals(status)) {
				return ALIVE;
			} else if (Status.STATUS_UNINITIALISED.equals(status)) {
				return UNINITIALISED;
			} else if (Status.STATUS_SHUTDOWN.equals(status)) {
				return SHUTDOWN;
			}
			return UNKNOWN;
		}
	}

}
