package org.iglooproject.test.config.bootstrap.spring.failures;

import org.assertj.core.api.Assertions;
import org.iglooproject.test.config.bootstrap.spring.util.broken.EmptyApplicationDescriptionConfig;
import org.junit.Test;

public class EmptyApplicationDescriptionFailureTest extends AbstractContextFailureTest {

	@Test
	public void noApplicationDescriptionsFailure() {
		Assertions.assertThatCode(() -> this.initializeContext(EmptyApplicationDescriptionConfig.class)).isInstanceOf(RuntimeException.class);
	}

}
