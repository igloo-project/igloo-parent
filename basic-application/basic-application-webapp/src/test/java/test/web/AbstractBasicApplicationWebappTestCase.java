package test.web;

import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.util.tester.WicketTester;
import org.iglooproject.basicapp.web.application.navigation.page.HomePage;
import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.iglooproject.test.wicket.more.AbstractWicketMoreTestCase;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import test.web.config.spring.BasicApplicationWebappTestCommonConfig;

@ContextConfiguration(classes = BasicApplicationWebappTestCommonConfig.class)
public class AbstractBasicApplicationWebappTestCase extends AbstractWicketMoreTestCase {

	@Autowired
	WebApplication application;

	@Before
	public void setUp() {
		setWicketTester(new WicketTester(application));
	}

	@Test
	public void homePageRendersSuccessfully() {
		getWicketTester().startPage(HomePage.class);
		
		getWicketTester().assertRenderedPage(HomePage.class);
	}

	@Override
	protected void cleanAll() throws ServiceException, SecurityServiceException {
		// TODO cleanEntities(service appelé);
	}

}
