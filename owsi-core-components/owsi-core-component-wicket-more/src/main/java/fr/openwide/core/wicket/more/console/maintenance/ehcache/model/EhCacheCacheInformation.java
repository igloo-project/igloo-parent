package fr.openwide.core.wicket.more.console.maintenance.ehcache.model;

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
		return cache.getStatistics().getMemoryStoreObjectCount();
	}
	
	public int getMaxElementsInMemory() {
		return cache.getCacheConfiguration().getMaxElementsInMemory();
	}
	
	public void setMaxElementsInMemory(int maxElementsInMemory) {
		cache.getCacheConfiguration().setMaxElementsInMemory(maxElementsInMemory);
	}
	
	public Float getCacheFillRatio() {
		if (getMaxElementsInMemory() + getMemoryStoreObjectCount() == 0) {
			return null;
		}
		
		return 100F * getMemoryStoreObjectCount() / (getMaxElementsInMemory() + 0F);
	}
	
	public long getCacheHits() {
		return cache.getStatistics().getCacheHits();
	}
	
	public long getCacheMisses() {
		return cache.getStatistics().getCacheMisses();
	}
	
	public Float getHitRatio() {
		if (getCacheHits() + getCacheMisses() == 0) {
			return null;
		}
		
		return 100F * getCacheHits() / (getCacheHits() + getCacheMisses() + 0F);
	}
	
	public long getEvictionCount() {
		return cache.getStatistics().getEvictionCount();
	}
	
	public long getAverageSearchTime() {
		return cache.getStatistics().getAverageSearchTime();
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
