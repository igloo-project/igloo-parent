package org.iglooproject.test.config.bootstrap.spring.failures;

import org.assertj.core.api.Assertions;
import org.iglooproject.test.config.bootstrap.spring.util.broken.TwoApplicationDescriptionConfig;
import org.junit.Test;

public class MultipleApplicationDescriptionFailureTest extends AbstractContextFailureTest {

	@Test
	public void multipleApplicationDescriptionsFailure() {
		Assertions.assertThatCode(() -> this.initializeContext(TwoApplicationDescriptionConfig.class)).isInstanceOf(RuntimeException.class);
	}

}
