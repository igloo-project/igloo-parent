package test.cache;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.net.URI;
import java.util.Optional;
import java.util.function.Function;
import org.iglooproject.jpa.autoconfigure.JCacheLookup;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.cache.jcache.JCacheCacheManager;

class TestJCacheLookup {
  /** Check Hibernate Level 2 JCache resolution when a classpath handler is setup (tomcat) */
  @Test
  void testClasspathURLStreamHandler() {
    // when ClasspathURLStreamHandler is here, hibernate cache is registered with classpath://
    // lookup with original url succeeds.
    // resolved uri is not used.
    Function<URI, JCacheCacheManager> provider = Mockito.<Function<URI, JCacheCacheManager>>mock();
    when(provider.apply(URI.create("classpath://placeholder")))
        .thenReturn(new JCacheCacheManager());
    JCacheLookup.lookup("classpath://placeholder", s -> Optional.of("file:/"), provider);
    verify(provider).apply(URI.create("classpath://placeholder"));
    verifyNoMoreInteractions(provider);
  }

  /**
   * Check Hibernate Level 2 JCache resolution when a classpath handler is not setup (test,
   * spring-boot launcher). Case where resolved uri is file:
   */
  @Test
  void testNoClasspathURLStreamHandler_file() {
    // when ClasspathURLStreamHandler is not here, hibernate cache is registered with resolved url
    // (file: or jar:)
    // we lookup original url (succeeds but it's empty) then resolved uri
    Function<URI, JCacheCacheManager> provider = Mockito.<Function<URI, JCacheCacheManager>>mock();
    when(provider.apply(URI.create("classpath://placeholder"))).thenReturn(null);
    when(provider.apply(URI.create("file:/"))).thenReturn(new JCacheCacheManager());
    JCacheLookup.lookup("classpath://placeholder", s -> Optional.of("file:/"), provider);
    verify(provider).apply(URI.create("classpath://placeholder"));
    verify(provider).apply(URI.create("file:/"));
    verifyNoMoreInteractions(provider);
  }

  /**
   * Check Hibernate Level 2 JCache resolution when a classpath handler is not setup (test,
   * spring-boot launcher). Case where resolved uri is jar:
   */
  @Test
  void testNoClasspathURLStreamHandler_jar() {
    // when ClasspathURLStreamHandler is not here, hibernate cache is registered with resolved url
    // (file: or jar:)
    // we lookup original url (succeeds but it's empty) then resolved uri
    Function<URI, JCacheCacheManager> provider = Mockito.<Function<URI, JCacheCacheManager>>mock();
    when(provider.apply(URI.create("classpath://placeholder"))).thenReturn(null);
    when(provider.apply(URI.create("jar:file:/"))).thenReturn(new JCacheCacheManager());
    JCacheLookup.lookup("classpath://placeholder", s -> Optional.of("jar:file:/"), provider);
    verify(provider).apply(URI.create("classpath://placeholder"));
    verify(provider).apply(URI.create("jar:file:/"));
    verifyNoMoreInteractions(provider);
  }
}
