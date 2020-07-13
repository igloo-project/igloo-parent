package org.iglooproject.wicket.more.console.maintenance.ehcache.model;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;

import org.apache.wicket.model.LoadableDetachableModel;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import org.iglooproject.jpa.business.generic.model.GenericEntity;

public class EhCacheCacheListModel extends LoadableDetachableModel<List<Cache>> {

	private static final long serialVersionUID = 2905490587596353984L;
	
	private String cacheManagerName;
	
	public EhCacheCacheListModel(CacheManager cacheManager) {
		cacheManagerName = cacheManager.getName();
	}

	@Override
	protected List<Cache> load() {
		List<Cache> caches = Lists.newArrayList();
		for (CacheManager cacheManager : CacheManager.ALL_CACHE_MANAGERS) {
			if (cacheManager.getName().equals(cacheManagerName)) {
				Set<String> cacheNames = Sets.newTreeSet(GenericEntity.STRING_COLLATOR_FRENCH);
				cacheNames.addAll(Arrays.asList(cacheManager.getCacheNames()));
				
				for (String cacheName : cacheNames) {
					caches.add(cacheManager.getCache(cacheName));
				}
			}
		}
		return caches;
	}

}
