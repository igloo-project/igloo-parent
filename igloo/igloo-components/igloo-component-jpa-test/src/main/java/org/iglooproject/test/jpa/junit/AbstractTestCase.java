package org.iglooproject.test.jpa.junit;

import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * JUnit5 base class for Igloo tests.
 */
@ExtendWith(SpringExtension.class)
public abstract class AbstractTestCase extends AbstractCommonTestCase {

	@BeforeEach
	@Override
	public void init() throws ServiceException, SecurityServiceException {
		super.init();
	}

	@AfterEach
	@Override
	public void close() throws ServiceException, SecurityServiceException {
		super.close();
	}

}
