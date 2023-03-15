package igloo.cache.monitor;

import org.springframework.cache.CacheManager;

/**
 * Object adding name to {@link CacheManager} contract. <code>name</code> is the spring context's bean bean.
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
		if (obj instanceof CacheManagerWrapper) {
			return ((CacheManagerWrapper) obj).getCacheManager().equals(this.getCacheManager());
		}
		return false;
	}

	public void clearAll() {
		cacheManager.getCacheNames().forEach(name -> cacheManager.getCache(name).clear());
	}

}
