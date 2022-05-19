package org.iglooproject.test.jpa.junit;

import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.springframework.test.context.junit4.rules.SpringClassRule;
import org.springframework.test.context.junit4.rules.SpringMethodRule;

/**
 * @deprecated Prefer junit5 tests if possible.
 */
@Deprecated(since = "4.1.0")
public abstract class AbstractJunit4TestCase extends AbstractTestCase {
	/**
	 * Use this instead of SpringJUnit4ClassRunner, so that implementors can choose their own runner
	 */
	@ClassRule
	public static final SpringClassRule SCR = new SpringClassRule();
	/**
	 * Use this instead of SpringJUnit4ClassRunner, so that implementors can choose their own runner
	 */
	@Rule
	public final SpringMethodRule springMethodRule = new SpringMethodRule();


	@Before
	@Override
	public void init() throws ServiceException, SecurityServiceException {
		super.init();
	}

	@After
	@Override
	public void close() throws ServiceException, SecurityServiceException {
		super.close();
	}

}
