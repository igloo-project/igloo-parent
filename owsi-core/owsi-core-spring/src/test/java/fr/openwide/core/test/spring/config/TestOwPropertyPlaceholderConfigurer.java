package fr.openwide.core.test.spring.config;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Test;

public class TestOwPropertyPlaceholderConfigurer {

	@Test
	public void testPlaceholder() throws IOException {
		TestConfigurer configurer = new TestConfigurer();
		
		assertEquals("value1", configurer.getProperty1());
		assertEquals("value2", configurer.getProperty2());
		assertEquals("value1-value2", configurer.getProperty3());
	}
	
}
