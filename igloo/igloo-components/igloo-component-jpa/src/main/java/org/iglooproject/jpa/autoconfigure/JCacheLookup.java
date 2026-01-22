package org.iglooproject.jpa.autoconfigure;

import com.github.benmanes.caffeine.jcache.spi.CaffeineCachingProvider;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Iterables;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import javax.cache.Caching;
import javax.cache.spi.CachingProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.CacheManager;
import org.springframework.cache.jcache.JCacheCacheManager;

public class JCacheLookup {

  private static final Logger LOGGER = LoggerFactory.getLogger(JCacheLookup.class);

  private static final String CLASSPATH_PREFIX = "classpath://";

  private JCacheLookup() {}

  public static CacheManager lookup(String locationUri) {
    return lookup(
        locationUri,
        JCacheLookup::resolveUri,
        uri -> JCacheLookup.findValidCandidate(uri, true),
        uri -> JCacheLookup.findValidCandidate(uri, false));
  }

  @VisibleForTesting
  public static CacheManager lookup(
      String locationUri,
      Function<String, Optional<String>> uriResolver,
      Function<URI, JCacheCacheManager> cacheProvider,
      Function<URI, JCacheCacheManager> fallbackProvider) {
    // Hibernate can perform some URI resolution when it creates cache manager. We need
    // to find the effective url used to register cache.
    // Cases:
    // 1. ClasspathURLStreamHandler is registered (tomcat native):
    // classpath:// URL is valid and untouched
    // classpath:// URL cannot be used to open resource with streamhandler (correct URL is
    // classpath:/)
    // We need to lookup original URL
    //
    // 2. ClasspathURLStreamHandler is not registered (tests, spring boot launcher):
    // classpath:// URL is invalid and transformed to file: (IDE, tests) or jar: (deployment) -
    // depends on the way to provide back module
    // We need to resolve location to the same effective URL
    //
    // classpath:/ cannot be used as Hibernate does not handle this URL (only classpath://)
    // see ClassLoaderServiceImpl#locateResource.
    // A direct path inside classpath may be used (without PROTOCOL:) but it is not a valid URL so
    // it is resolved by Hibernate (key is the file: or jar:).
    //
    // To handle all the cases, we just try the two candidates: raw and resolved URI
    // We keep the first not-empty CacheManager
    // We emit an error if no not-empty CacheManager can be retrieved
    // classpath:/ (not classpath://) values are rejected
    Set<String> uriCandidates = new LinkedHashSet<>(); // preserve insertion order
    // test with original URI
    uriCandidates.add(locationUri);
    // add resolved classpath:// URI (only if well-known)
    uriResolver.apply(locationUri).ifPresent(uriCandidates::add);

    LOGGER.info("Trying to locate Hibernate Level 2 cache from {}", uriCandidates);
    // try candidates ; return first valid (not-empty), or fallback with default
    return uriCandidates.stream()
        .map(URI::create)
        .map(cacheProvider)
        .filter(Objects::nonNull)
        .findFirst()
        .orElseGet(() -> fallbackProvider.apply(URI.create(locationUri)));
  }

  private static Optional<String> resolveUri(String uri) {
    if (uri.startsWith(CLASSPATH_PREFIX)) {
      try {
        // Use a classpath resource (without scheme) to find working URI
        String pathWithoutLeadingSlash = uri.substring(CLASSPATH_PREFIX.length());
        URI resolved =
            Thread.currentThread()
                .getContextClassLoader()
                .getResource(pathWithoutLeadingSlash)
                .toURI();
        // Use only well-known URIs
        if ("file".equals(resolved.getScheme()) || "jar".equals(resolved.getScheme())) {
          return Optional.of(resolved.toString());
        }
      } catch (RuntimeException | URISyntaxException e) {
        // nothing
      }
    }
    return Optional.empty();
  }

  /**
   * Lookup caffeine cache with uri and checks if cache is empty. We expect a not-empty cache as
   * region must be already created by Hibernate.
   */
  private static JCacheCacheManager findValidCandidate(URI uri, boolean ensureNotEmpty) {
    CachingProvider cachingProvider =
        Caching.getCachingProvider(CaffeineCachingProvider.class.getName());
    javax.cache.CacheManager cacheManager = cachingProvider.getCacheManager(uri, null);
    int cacheSize = Iterables.size(cacheManager.getCacheNames());
    if (ensureNotEmpty && cacheSize == 0) {
      LOGGER.info("CacheManager for {} is empty. Ignored as Hibernate Level 2 cache.", uri);
      // we cannot close cacheManager as if cache is enabled but no entity is cachable, we
      // may have a valid hibernate level 2 cache with 0 region. We do not want to close
      // this cache. Just keep the orphan empty cache.
      cacheManager.close();
    } else {
      LOGGER.info("Hibernate Level 2 cache found with {} ({} caches)", uri, cacheSize);
      var springCacheManager = new JCacheCacheManager(cacheManager);
      springCacheManager.afterPropertiesSet();
      return springCacheManager;
    }
    return null;
  }
}
