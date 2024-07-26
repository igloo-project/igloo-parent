package org.iglooproject.test.spring;

import static org.assertj.core.api.Assertions.anyOf;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.regex.Pattern;
import org.assertj.core.api.Condition;
import org.iglooproject.config.bootstrap.spring.annotations.ManifestPropertySource;
import org.iglooproject.spring.config.spring.IglooVersionInfoConfig;
import org.iglooproject.test.AbstractJpaCoreTestCase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;

class SpringInformationsTest extends AbstractJpaCoreTestCase {

  @Value("${igloo.version}")
  private String version;

  @Value("${igloo.build.user.name}")
  private String buildUserName;

  /**
   * @see IglooVersionInfoConfig
   * @see ManifestPropertySource
   */
  @Test
  void iglooInformations() {
    // test that igloo version is correctly read from MANIFEST files
    // deployed jar provides version number
    Condition<String> real =
        new Condition<>(s -> Pattern.matches("[0-9]\\.[0-9].*", s), "A normal version pattern");
    // maven test provides only a fake MANIFEST.MF
    Condition<String> workaround = new Condition<>("placeholder"::equals, "A placeholder pattern");
    Condition<String> versionCondition = anyOf(real, workaround);
    assertThat(version).is(versionCondition);
    assertThat(buildUserName).isNotBlank();
  }
}
