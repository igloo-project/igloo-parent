package org.iglooproject.test.config.bootstrap.spring.failures;

import org.assertj.core.api.Assertions;
import org.iglooproject.test.config.bootstrap.spring.util.broken.NoApplicationDescriptionConfig;
import org.junit.Test;

public class NoApplicationDescriptionFailureTest extends AbstractContextFailureTest {

	@Test
	public void noApplicationDescriptionsFailure() {
		Assertions.assertThatCode(() -> this.initializeContext(NoApplicationDescriptionConfig.class)).isInstanceOf(RuntimeException.class);
	}

}
