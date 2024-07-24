package org.iglooproject.test.config.bootstrap.spring.manifest;

import org.assertj.core.api.Assertions;
import org.iglooproject.config.bootstrap.autoconfigure.ManifestAutoConfiguration;
import org.iglooproject.config.bootstrap.spring.annotations.ManifestPropertySource;
import org.iglooproject.test.config.bootstrap.spring.manifest.ManifestPropertySourceTest.TestManifestConfiguration;
import org.iglooproject.test.config.bootstrap.spring.util.AbstractTestCase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = {ManifestAutoConfiguration.class, TestManifestConfiguration.class})
class ManifestPropertySourceTest extends AbstractTestCase {

  @Configuration
  @ManifestPropertySource(prefix = "test")
  public static class TestManifestConfiguration {}

  @Value("${test.Implementation-Version}")
  private String version;

  @Value("${test.Implementation-Title}")
  private String title;

  @Value("${test.Built-By}")
  private String builtBy;

  @Test
  void manifest() {
    Assertions.assertThat(version).isEqualTo("test-version");
    Assertions.assertThat(title).isEqualTo("test-title");
    Assertions.assertThat(builtBy).isEqualTo("test-builtBy");
  }
}
