package org.iglooproject.test.spring;

import static org.assertj.core.api.Assertions.assertThat;

import org.iglooproject.config.bootstrap.spring.annotations.ManifestPropertySource;
import org.iglooproject.spring.config.spring.IglooVersionInfoConfig;
import org.iglooproject.test.AbstractJpaCoreTestCase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Value;

public class SpringInformationsTest extends AbstractJpaCoreTestCase {

	@Value("${igloo.version}")
	private String version;
	@Value("${build.user.name}")
	private String buildUserName;

	/**
	 * @see IglooVersionInfoConfig
	 * @see ManifestPropertySource
	 */
	@Test
	public void iglooInformations() {
		// test that igloo version is correctly read from MANIFEST files
		assertThat(version).matches("[0-9]\\.[0-9].*");
		assertThat(buildUserName).isNotBlank();
	}
}
