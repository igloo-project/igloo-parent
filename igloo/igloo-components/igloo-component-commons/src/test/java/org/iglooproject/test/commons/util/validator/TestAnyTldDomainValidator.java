package org.iglooproject.test.commons.util.validator;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import org.iglooproject.commons.util.validator.AnyTldDomainValidator;

@RunWith(Parameterized.class)
public class TestAnyTldDomainValidator {
	
	@Parameters(name = "{index} - \"{0}\" expecting {1}")
	public static Iterable<Object[]> data() {
		return Arrays.asList(new Object[][] {
				{ "www.google.fr", true },
				{ "google.fr", true },
				{ "google9.fr", true },
				{ "gîte-lasoldanelle.com", true },
				{ "www.gîte-lasoldanelle.com", true },
				{ "île-lasoldanelle.com", true },
				{ "location_maechler.monsite-orange.fr", true },
				{ "-test-plop.fr", false },
				{ "test-plop-.fr", false },
				{ "test-plop-.fr", false },
				{ "test@-plop.fr", false },
				{ "test,-plop.fr", false }
		});
	}
	
	@Parameter(0)
	public String domain;
	
	@Parameter(1)
	public boolean expectedValid;
	
	@Test
	public void testValidation() {
		AnyTldDomainValidator validator = AnyTldDomainValidator.getInstance();
		if (expectedValid) {
			Assert.assertTrue(validator.isValid(domain));
		} else {
			Assert.assertFalse(validator.isValid(domain));
		}
	}
}
