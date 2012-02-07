package fr.openwide.core.test.spring.config;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners({
	DependencyInjectionTestExecutionListener.class
})
@ContextConfiguration(locations = {
	"classpath:test/testOwPropertyPlaceholderConfigurer-context.xml"
})
public class TestOwPropertyPlaceholderConfigurer {

	@Autowired
	private OwsiCoreTestConfigurer configurer;

	@Test
	public void testPlaceholder() throws IOException {
		assertEquals("value1", configurer.getProperty1());
		assertEquals("value2", configurer.getProperty2());
		assertEquals("value1-value2", configurer.getProperty3());
		assertEquals("value3", configurer.getProperty4());
		
		assertEquals(OwsiCoreTestConfigurer.PROPERTY6_DEFAULT, configurer.getProperty6());
	}
}
