package org.iglooproject.jpa.autoconfigure;

import com.github.benmanes.caffeine.jcache.spi.CaffeineCachingProvider;
import jakarta.persistence.EntityManagerFactory;
import java.net.URISyntaxException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Expose second-level cache a spring bean. This is done to allow to build a common console page for
 * monitoring spring and hibernate cache.
 */
@Configuration
@ConditionalOnExpression("'${spring.jpa.properties.hibernate.javax.cache.uri:none}' != 'none'")
public class JpaCacheAutoConfiguration {

  @Bean
  public CacheManager jpaCacheManager(
      EntityManagerFactory entityManagerFactory,
      @Value("${spring.jpa.properties.hibernate.javax.cache.provider}") String cacheProvider,
      @Value("${spring.jpa.properties.hibernate.javax.cache.uri}") String locationUri)
      throws URISyntaxException {
    if (CaffeineCachingProvider.class.getName().equals(cacheProvider)) {
      return org.iglooproject.jpa.autoconfigure.JCacheLookup.lookup(locationUri);
    } else {
      throw new IllegalStateException();
    }
  }
}
