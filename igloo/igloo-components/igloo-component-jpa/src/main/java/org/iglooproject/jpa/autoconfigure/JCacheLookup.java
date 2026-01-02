package org.iglooproject.jpa.autoconfigure;

import com.github.benmanes.caffeine.jcache.spi.CaffeineCachingProvider;
import com.google.common.collect.Iterables;
import java.net.URI;
import java.net.URISyntaxException;
import javax.cache.Caching;
import javax.cache.spi.CachingProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.CacheManager;
import org.springframework.cache.jcache.JCacheCacheManager;

public class JCacheLookup {

  private static final Logger LOGGER = LoggerFactory.getLogger(JCacheLookup.class);

  private JCacheLookup() {}

  public static CacheManager lookup(String locationUri) throws URISyntaxException {
    CachingProvider cachingProvider =
        Caching.getCachingProvider(CaffeineCachingProvider.class.getName());
    URI cacheManagerUri = URI.create(locationUri);
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
      try (var is = cacheManagerUri.toURL().openStream()) {
        // NOTHING, just check if url classpath handler is here
      } catch (Exception ignore) {
        try {
          // Use a classpath resource (without scheme) to find working URI
          String pathWithoutLeadingSlash =
              cacheManagerUri.toString().substring("classpath://".length());
          URI resolved =
              Thread.currentThread()
                  .getContextClassLoader()
                  .getResource(pathWithoutLeadingSlash)
                  .toURI();
          // Use only well-known URIs
          if ("file".equals(resolved.getScheme()) || "jar".equals(resolved.getScheme())) {
            cacheManagerUri = resolved;
          }
        } catch (URISyntaxException e) {
          // Nothing, there is a warn emitted if cache manager is empty
        }
      }
    }
    javax.cache.CacheManager cacheManager = cachingProvider.getCacheManager(cacheManagerUri, null);
    if (Iterables.isEmpty(cacheManager.getCacheNames())) {
      LOGGER.warn("CacheManager for {} is unexpectedly empty.", locationUri);
    }
    var springCacheManager = new JCacheCacheManager(cacheManager);
    springCacheManager.afterPropertiesSet();
    return springCacheManager;
  }
}
