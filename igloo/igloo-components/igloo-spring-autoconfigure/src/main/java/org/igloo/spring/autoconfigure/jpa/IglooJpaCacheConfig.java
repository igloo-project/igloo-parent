package org.igloo.spring.autoconfigure.jpa;

import java.net.URISyntaxException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;

import com.github.benmanes.caffeine.jcache.spi.CaffeineCachingProvider;

import jakarta.persistence.EntityManagerFactory;

/**
 * Expose second-level cache a spring bean. This is done to allow to build a common console page for monitoring
 * spring and hibernate cache.
 */
@ConditionalOnExpression("'${spring.jpa.properties.hibernate.javax.cache.uri:none}' != 'none'")
public class IglooJpaCacheConfig {

	@Bean
	public JpaCacheManagerWrapper jpaCacheManager(
			EntityManagerFactory entityManagerFactory,
			@Value("${spring.jpa.properties.hibernate.javax.cache.provider}") String cacheProvider,
			@Value("${spring.jpa.properties.hibernate.javax.cache.uri}") String locationUri) throws URISyntaxException {
		if (CaffeineCachingProvider.class.getName().equals(cacheProvider)) {
			return new JpaCacheManagerWrapper(org.igloo.spring.autoconfigure.jpa.JCacheLookup.lookup(locationUri));
		} else {
			throw new IllegalStateException();
		}
	}

	/**
	 * Hide cacheManager in a wrapper (as spring-boot is difficult to configure with multiple {@link CacheManager} beans). 
	 */
	public class JpaCacheManagerWrapper {
		private final CacheManager cacheManager;
		
		public JpaCacheManagerWrapper(CacheManager cacheManager) {
			this.cacheManager = cacheManager;
		}
		
		public CacheManager getCacheManager() {
			return cacheManager;
		}
	}
}
