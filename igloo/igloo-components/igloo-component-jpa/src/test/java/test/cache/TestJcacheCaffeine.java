package test.cache;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.URI;
import java.util.stream.IntStream;

import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.configuration.MutableConfiguration;
import javax.cache.spi.CachingProvider;

import org.junit.jupiter.api.Test;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.jcache.CacheManagerImpl;
import com.github.benmanes.caffeine.jcache.spi.CaffeineCachingProvider;

/**
 * <p>Check that JSR-107 allows to load a separate configuration file by using {@link URI} argument when
 * {@link CacheManager} is created. This is needed so that <code>hibernate.javax.cache.uri</code> property can
 * be used to load a separate root configuration. This is wanted to allow a sensible and specific default
 * configuration for hibernate second-level caching.</p>
 * 
 * <p>Also test that basic statistics and configuration methods work as expected.</p>
 */
class TestJcacheCaffeine {

	@Test
	void testDefaultConfig() {
		CacheManager cacheManager = createJsr107CacheManager(URI.create("default"));
		var cache = createJsr107Cache(cacheManager, "fallback");
		cache.put("key", "value");
		assertThat(cache.get("key")).isEqualTo("value");
		
		// check default configuration (uri does not target an existing configuration)
		String cacheName = "fallback";
		Cache<Object, Object> fallbackCache = getNativeCache(cacheManager, cacheName);
		fallbackCache.cleanUp();
		assertThat(fallbackCache.policy().eviction()).isEmpty();
	}

	@Test
	void testCustomConfig() {
		CacheManager cacheManager = createJsr107CacheManager(URI.create("classpath:caffeine-custom.conf"));
		var cache = createJsr107Cache(cacheManager, "fallback");
		cache.put("key", "value");
		assertThat(cache.get("key")).isEqualTo("value");
		
		// perform a miss
		cache.get("missingKey");
		
		// check custom configuration (uri target a configuration file)
		Cache<Object, Object> fallbackCache = getNativeCache(cacheManager, "fallback");
		fallbackCache.cleanUp();
		// check that entries never expire
		assertThat(fallbackCache.policy().expireAfterWrite()).isEmpty();
		assertThat(fallbackCache.policy().expireAfterAccess()).isEmpty();
		assertThat(fallbackCache.policy().expireVariably()).isEmpty();
		// check default max size
		assertThat(fallbackCache.policy().eviction().get().getMaximum()).isEqualTo(10l);
		// check activity stats
		assertThat(fallbackCache.estimatedSize()).isEqualTo(1l);
		assertThat(fallbackCache.stats().hitCount()).isEqualTo(1);
		assertThat(fallbackCache.stats().missCount()).isEqualTo(1);

		Cache<Object, Object> overriddenCache = getNativeCache(cacheManager, "overridden");
		overriddenCache.cleanUp();
		// check customization
		assertThat(overriddenCache.policy().eviction().get().getMaximum()).isEqualTo(20l);
		assertThat(overriddenCache.policy().eviction().get().isWeighted()).isFalse();
		assertThat(overriddenCache.estimatedSize()).isZero();
		
		// push items to trigger evictions
		IntStream.range(0, 10).forEach(i -> cache.put(i, i));
		
		fallbackCache.cleanUp();
		// check activity stats
		assertThat(fallbackCache.estimatedSize()).isEqualTo(10l);
		assertThat(fallbackCache.stats().hitCount()).isEqualTo(1);
		assertThat(fallbackCache.stats().missCount()).isEqualTo(1);
		assertThat(fallbackCache.stats().evictionCount()).isEqualTo(1);
		
		// reconfigure and check cache update
		fallbackCache.policy().eviction().get().setMaximum(5);
		fallbackCache.cleanUp();
		assertThat(fallbackCache.estimatedSize()).isEqualTo(5l);
		assertThat(fallbackCache.stats().evictionCount()).isEqualTo(6);
	}

	@Test
	void testDottedName() {
		CacheManager cacheManager = createJsr107CacheManager(URI.create("classpath:caffeine-dotted-name.conf"));
		var dottedNameCache = getNativeCache(cacheManager, "dotted.name");
		assertThat(dottedNameCache.policy().eviction().get().getMaximum()).isEqualTo(20l);
	}

	private CacheManager createJsr107CacheManager(URI configUri) {
		CachingProvider cacheProvider = Caching.getCachingProvider(CaffeineCachingProvider.class.getName());
		CacheManager cacheManager = cacheProvider.getCacheManager(configUri, null);
		return cacheManager;
	}

	private javax.cache.Cache<Object, Object> createJsr107Cache(CacheManager cacheManager, String cacheName) {
		return cacheManager.createCache(cacheName, new MutableConfiguration<>());
	}

	@SuppressWarnings("unchecked")
	private Cache<Object, Object> getNativeCache(CacheManager cacheManager, String cacheName) {
		return cacheManager.unwrap(CacheManagerImpl.class)
			.getCache(cacheName)
			.unwrap(Cache.class);
	}

}
