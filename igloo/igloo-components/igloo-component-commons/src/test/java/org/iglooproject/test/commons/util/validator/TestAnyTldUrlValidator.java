package org.iglooproject.test.commons.util.validator;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import org.iglooproject.commons.util.validator.AnyTldUrlValidator;

@RunWith(Parameterized.class)
public class TestAnyTldUrlValidator {
	
	@Parameters(name = "{index} - \"{0}\" expecting {1}")
	public static Iterable<Object[]> data() {
		return Arrays.asList(new Object[][] {
				{ "http://www.google.fr/test", true },
				{ "http://google.fr/test/2", true },
				{ "http://google9.fr/test/2", true },
				{ "http://gîte-lasoldanelle.com/test/2?param=value", true },
				{ "http://www.gîte-lasoldanelle.com/test/2?param=value", true },
				{ "http://île-lasoldanelle.com/test/2?param=value", true },
				{ "http://location_maechler.monsite-orange.fr/test/2?param=value", true },
				{ "http://-test-plop.fr", false },
				{ "http://test-plop-.fr", false },
				{ "http://test-plop-.fr", false },
				{ "http://test@-plop.fr", false },
				{ "http://test,-plop.fr", false }
		});
	}
	
	@Parameter(0)
	public String url;
	
	@Parameter(1)
	public boolean expectedValid;
	
	@Test
	public void testValidation() {
		AnyTldUrlValidator validator = AnyTldUrlValidator.getInstance();
		if (expectedValid) {
			Assert.assertTrue(validator.isValid(url));
		} else {
			Assert.assertFalse(validator.isValid(url));
		}
	}

}
