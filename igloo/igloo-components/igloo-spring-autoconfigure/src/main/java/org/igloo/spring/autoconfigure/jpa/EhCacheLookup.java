package org.igloo.spring.autoconfigure.jpa;

import org.iglooproject.jpa.config.spring.provider.IJpaConfigurationProvider;
import org.springframework.cache.CacheManager;
import org.springframework.cache.ehcache.EhCacheCacheManager;

public class EhCacheLookup {

	private EhCacheLookup() {}

	public static CacheManager lookup(IJpaConfigurationProvider jpaConfigurationProvider) {
		return new EhCacheCacheManager(net.sf.ehcache.CacheManager.ALL_CACHE_MANAGERS.stream() //NOSONAR
				.filter(cm -> "hibernateCacheManager".equals(cm.getName()))
				.findAny().get());
	}

}
