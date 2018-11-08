package org.iglooproject.basicapp.web.test;

import org.apache.wicket.util.tester.WicketTester;
import org.iglooproject.basicapp.web.application.BasicApplicationApplication;
import org.iglooproject.basicapp.web.application.navigation.page.HomePage;
import org.iglooproject.basicapp.web.test.config.spring.BasicApplicationWebappTestCommonConfig;
import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.iglooproject.test.wicket.more.AbstractWicketMoreTestCase;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(
		classes = BasicApplicationWebappTestCommonConfig.class,
		inheritInitializers = true
)
public class AbstractBasicApplicationWebappTestCase extends AbstractWicketMoreTestCase {

	@Before
	public void setUp() {
		setWicketTester(new WicketTester(new BasicApplicationApplication()));
	}

	@Test
	public void homepageRendersSuccessfully() {
		getWicketTester().startPage(HomePage.class);
		
		getWicketTester().assertRenderedPage(HomePage.class);
	}

	@Override
	protected void cleanAll() throws ServiceException, SecurityServiceException {
		// TODO cleanEntities(service appelé);
	}

}
