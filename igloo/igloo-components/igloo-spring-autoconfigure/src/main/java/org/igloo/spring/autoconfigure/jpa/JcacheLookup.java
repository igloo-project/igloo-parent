package org.igloo.spring.autoconfigure.jpa;

import com.github.benmanes.caffeine.jcache.spi.CaffeineCachingProvider;
import com.google.common.collect.Iterables;
import java.net.URI;
import java.net.URISyntaxException;
import javax.cache.Caching;
import javax.cache.spi.CachingProvider;
import org.iglooproject.jpa.config.spring.provider.IJpaConfigurationProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.CacheManager;
import org.springframework.cache.jcache.JCacheCacheManager;

public class JcacheLookup {

  private static final Logger LOGGER = LoggerFactory.getLogger(JcacheLookup.class);

  private JcacheLookup() {}

  public static CacheManager lookup(IJpaConfigurationProvider jpaConfigurationProvider) {
    CachingProvider cachingProvider =
        Caching.getCachingProvider(CaffeineCachingProvider.class.getName());
    URI cacheManagerUri = URI.create(jpaConfigurationProvider.getJcacheConfiguration());
    // hibernate perform some URI resolution when it creates cache manager;
    // we need to manipulate URI to get a chance to retrieve cache manager from caffeine provider
    // see org.hibernate.cache.jcache.internal.JCacheRegionFactory.getUri(SessionFactoryOptions,
    // Map)
    // Behavior depends on the availability of classpath:// resource handler
    // - all cases: caffeine does not modify provided URI
    // - no handler:
    // hibernate modifies classpath://FILE to resolved value (file:/ or jar: URI)
    // - handler available:
    // hibernate lets URI unmodified
    if ("classpath".equals(cacheManagerUri.getScheme())) {
      try {
        cacheManagerUri.toURL();
      } catch (Exception ignore) {
        try {
          // Nothing
          String pathWithoutLeadingSlash =
              cacheManagerUri.toString().substring("classpath://".length());
          URI resolved =
              Thread.currentThread()
                  .getContextClassLoader()
                  .getResource(pathWithoutLeadingSlash)
                  .toURI();
          if ("file".equals(resolved.getScheme())) {
            cacheManagerUri = resolved;
          }
        } catch (URISyntaxException e) {
          // Nothing, there is a warn emitted if cache manager is empty
        }
      }
    }
    javax.cache.CacheManager cacheManager = cachingProvider.getCacheManager(cacheManagerUri, null);
    if (Iterables.isEmpty(cacheManager.getCacheNames())) {
      LOGGER.warn(
          "CacheManager for {} is unexpectedly empty.",
          jpaConfigurationProvider.getJcacheConfiguration());
    }
    return new JCacheCacheManager(cacheManager);
  }
}
