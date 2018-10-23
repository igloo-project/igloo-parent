package org.iglooproject.basicapp.web.test;

import org.apache.wicket.util.tester.WicketTester;
import org.iglooproject.basicapp.web.application.BasicApplicationApplication;
import org.iglooproject.basicapp.web.application.navigation.page.HomePage;
import org.iglooproject.basicapp.web.test.config.spring.BasicApplicationWebappTestCommonConfig;
import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.iglooproject.spring.property.SpringPropertyIds;
import org.iglooproject.spring.property.service.IPropertyService;
import org.iglooproject.test.jpa.junit.AbstractTestCase;
import org.iglooproject.test.wicket.more.junit.IWicketTestCase;
import org.iglooproject.test.wicket.more.junit.WicketTesterTestExecutionListener;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;

@ContextConfiguration(classes = BasicApplicationWebappTestCommonConfig.class)
@TestExecutionListeners({ WicketTesterTestExecutionListener.class, DirtiesContextTestExecutionListener.class })
@DirtiesContext
public class AbstractBasicApplicationWebappTestCase extends AbstractTestCase implements IWicketTestCase {

	private WicketTester tester;

	@Autowired
	private IPropertyService propertyService;

	@Before
	public void setUp() {
		propertyService.get(SpringPropertyIds.CONFIGURATION_TYPE);
		tester = new WicketTester(new BasicApplicationApplication());
	}

	@Test
	public void homepageRendersSuccessfully() {
		
		
		tester.startPage(HomePage.class);
		
		tester.assertRenderedPage(HomePage.class);
	}

	@Override
	public void setWicketTester(WicketTester tester) {
		this.tester = tester;
	}

	@Override
	protected void cleanAll() throws ServiceException, SecurityServiceException {
		// TODO cleanEntities(service appelé);
	}

}
