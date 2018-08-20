package org.iglooproject.test.config.bootstrap.spring.manifest;

import org.assertj.core.api.Assertions;
import org.iglooproject.test.config.bootstrap.spring.util.AbstractTestCase;
import org.iglooproject.test.config.bootstrap.spring.util.ManifestConfig;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = ManifestConfig.class)
public class ManifestPropertySourceTest extends AbstractTestCase {

	@Value("${test.Implementation-Version}")
	private String version;
	@Value("${test.Implementation-Title}")
	private String title;
	@Value("${test.Built-By}")
	private String builtBy;

	@Test
	public void manifest() {
		Assertions.assertThat(version).isEqualTo("test-version");
		Assertions.assertThat(title).isEqualTo("test-title");
		Assertions.assertThat(builtBy).isEqualTo("test-builtBy");
	}

}
