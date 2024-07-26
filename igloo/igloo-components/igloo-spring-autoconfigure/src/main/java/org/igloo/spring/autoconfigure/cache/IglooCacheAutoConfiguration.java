package org.igloo.spring.autoconfigure.cache;

import igloo.cache.monitor.CacheManagerWrapper;
import igloo.cache.spring.CacheManagerRegistry;
import igloo.cache.spring.ICacheManagerRegistry;
import java.util.Map;
import java.util.stream.Collectors;
import org.igloo.spring.autoconfigure.jpa.IglooJpaCacheConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.cache.CacheAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

/**
 * Configure {@link Cacheable} annotation, spring caching subsystem and register a cache registry
 * needed by cache console page.
 *
 * <p>Cache configuration is done with spring boot <code>spring.cache</code> properties. Please note
 * that hibernate second-level cache is configured and initialized independantly.
 *
 * <p>Igloo supports caffeine and caffeine + jcache caches.
 *
 * <p>A not customized initialization use a <code>cacheManager</code> initialized by
 * spring/spring-boot subsystem (used for spring usage - {@link Cacheable} proxies, ...) and a
 * <code>jpaCacheManager</code> (used for second-level cachine) initialized by Hibernate and exposed
 * as a bean for monitoring purpose.
 *
 * @see IglooJpaCacheConfig
 */
@ConditionalOnProperty(
    name = "igloo-ac.cache.disabled",
    havingValue = "false",
    matchIfMissing = true)
@ConditionalOnClass({CacheManagerWrapper.class, CacheManager.class, ICacheManagerRegistry.class})
@Import(CacheAutoConfiguration.class)
@EnableCaching
public class IglooCacheAutoConfiguration extends CachingConfigurerSupport {

  private static final Logger LOGGER = LoggerFactory.getLogger(IglooCacheAutoConfiguration.class);

  @Bean
  public ICacheManagerRegistry cacheManagerRegitry(Map<String, CacheManager> cms) {
    if (LOGGER.isInfoEnabled()) {
      LOGGER.info(
          "Registering cache managers: {}",
          cms.keySet().stream().collect(Collectors.joining(", ")));
    }
    return new CacheManagerRegistry(cms);
  }

  @Qualifier("cacheManager")
  @Autowired
  private CacheManager cacheManager;

  @Override
  public CacheManager cacheManager() {
    return cacheManager;
  }
}
