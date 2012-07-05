package fr.openwide.core.wicket.more.console.maintenance.ehcache.model;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;

import org.apache.wicket.model.LoadableDetachableModel;

public class EhCacheCacheInformationModel extends LoadableDetachableModel<EhCacheCacheInformation> {

	private static final long serialVersionUID = 2905490587596353984L;
	
	private String cacheManagerName;
	
	private String cacheName;
	
	public EhCacheCacheInformationModel(Cache cache) {
		cacheManagerName = cache.getCacheManager().getName();
		cacheName = cache.getName();
	}

	@Override
	protected EhCacheCacheInformation load() {
		for (CacheManager cacheManager : CacheManager.ALL_CACHE_MANAGERS) {
			if (cacheManager.getName().equals(cacheManagerName)) {
				return new EhCacheCacheInformation(cacheManager.getCache(cacheName));
			}
		}
		return null;
	}

}
