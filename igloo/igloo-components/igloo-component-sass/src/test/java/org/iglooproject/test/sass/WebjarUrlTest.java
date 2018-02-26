package org.iglooproject.test.sass;

import static org.assertj.core.api.Assertions.assertThat;

import org.iglooproject.sass.util.JSassWebjarUrlMatcher;
import org.iglooproject.sass.util.JSassWebjarUrlMatcher.WebjarUrl;
import org.junit.Test;

public class WebjarUrlTest {

	private JSassWebjarUrlMatcher matcher = JSassWebjarUrlMatcher.INSTANCE;

	@Test
	public void versionLessUrl() {
		WebjarUrl url = matcher.match("webjars://webjar/path/to/resource");
		assertThat(url).isNotNull();
		assertThat(url.getProtocol()).isEqualTo("webjars");
		assertThat(url.getWebjar()).isEqualTo("webjar");
		assertThat(url.getResourcePath()).isEqualTo("/path/to/resource");
		assertThat(url.getVersion()).isNull();
	}

	@Test
	public void versionUrl() {
		WebjarUrl url = matcher.match("webjars://webjar:version/path/to/resource");
		assertThat(url).isNotNull();
		assertThat(url.getProtocol()).isEqualTo("webjars");
		assertThat(url.getWebjar()).isEqualTo("webjar");
		assertThat(url.getResourcePath()).isEqualTo("/path/to/resource");
		assertThat(url.getVersion()).isEqualTo("version");
	}

	@Test
	public void notUrl() {
		assertThat(matcher.match("./simple/relative/path.scss")).isNull();
		assertThat(matcher.match("http://www.iglooproject.org")).isNull();
		assertThat(matcher.match("file:/absolute/path.scss")).isNull();
	}

}
