package org.igloo.spring.autoconfigure.jpa;

import org.iglooproject.jpa.config.spring.provider.IJpaConfigurationProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;

/**
 * Expose second-level cache a spring bean. This is done to allow to build a common console page for monitoring
 * spring and hibernate cache.
 */
@ConditionalOnExpression("'${hibernate.cache:none}' != 'none'")
public class IglooJpaCacheConfig {

	@Bean
	public CacheManager jpaCacheManager(IJpaConfigurationProvider jpaConfigurationProvider) {
		switch(jpaConfigurationProvider.getEhCacheRegionFactory()) {
		case JCACHE_CAFFEINE:
			// keep inlined class name as we need a working code even if class is not on classpath
			// we use jcache wrapping as spring-caffeine cannot wrap an already existing cache manager
			return org.igloo.spring.autoconfigure.jpa.JcacheLookup.lookup(jpaConfigurationProvider);
		case NONE:
		default:
			// not reachable
			throw new IllegalStateException();
		}
	}
}
