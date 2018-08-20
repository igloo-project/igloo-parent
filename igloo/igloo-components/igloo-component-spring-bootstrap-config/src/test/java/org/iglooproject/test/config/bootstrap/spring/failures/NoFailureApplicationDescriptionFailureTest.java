package org.iglooproject.test.config.bootstrap.spring.failures;

import org.assertj.core.api.Assertions;
import org.iglooproject.test.config.bootstrap.spring.util.SpringConfig;
import org.junit.Test;

public class NoFailureApplicationDescriptionFailureTest extends AbstractContextFailureTest {

	@Test
	public void noApplicationDescriptionsFailure() {
		// ensure that there is not failure on standard path
		Assertions.assertThatCode(() -> this.initializeContext(SpringConfig.class)).doesNotThrowAnyException();
	}

}
