package org.iglooproject.config.bootstrap.spring.env;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.springframework.boot.env.PropertiesPropertySourceLoader;
import org.springframework.core.Ordered;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.Resource;

/**
 * {@link PropertiesPropertySourceLoader} that reads Spring Boot config files
 * (application*.properties, spring.config.import, ...) as UTF-8 instead of ISO-8859-1 when no
 * explicit encoding is given.
 *
 * <p>An explicit encoding hint (e.g. {@code classpath:file.properties[encoding=iso-8859-1]}) still
 * takes precedence.
 *
 * <p>Registered in META-INF/spring.factories with highest precedence so that it is chosen before
 * Spring Boot's default {@link PropertiesPropertySourceLoader} for .properties files.
 */
public class Utf8PropertiesPropertySourceLoader extends PropertiesPropertySourceLoader
    implements Ordered {

  @Override
  public List<PropertySource<?>> load(String name, Resource resource, Charset encoding)
      throws IOException {
    return super.load(name, resource, encoding != null ? encoding : StandardCharsets.UTF_8);
  }

  @Override
  public int getOrder() {
    return Ordered.HIGHEST_PRECEDENCE;
  }
}
