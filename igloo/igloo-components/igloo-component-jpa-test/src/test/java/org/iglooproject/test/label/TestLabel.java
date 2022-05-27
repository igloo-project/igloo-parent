package org.iglooproject.test.label;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.iglooproject.test.AbstractJpaCoreTestCase;
import org.iglooproject.test.business.label.model.Label;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TestLabel extends AbstractJpaCoreTestCase {
	
	@Test
	void testLabel() throws ServiceException, SecurityServiceException {
		Label label1 = new Label("label1", "value1");
		labelService.create(label1);
		
		Label label2 = new Label("label2", "value2");
		labelService.create(label2);
		
		assertEquals(2, (long) labelService.count());
		
		assertEquals(label1, labelService.getById("label1"));
		assertEquals(-1, label1.compareTo(label2));
		
		labelService.delete(label2);
		
		assertTrue(labelService.count() == 1);
	}
	
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
