package fr.openwide.core.test.label;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import fr.openwide.core.jpa.exception.SecurityServiceException;
import fr.openwide.core.jpa.exception.ServiceException;
import fr.openwide.core.test.AbstractJpaCoreTestCase;
import fr.openwide.core.test.jpa.example.business.label.model.Label;

public class TestLabel extends AbstractJpaCoreTestCase {
	
	@Test
	public void testLabel() throws ServiceException, SecurityServiceException {
		Label label1 = new Label("label1", "value1");
		labelService.create(label1);
		
		Label label2 = new Label("label2", "value2");
		labelService.create(label2);
		
		Assert.assertEquals(2, (long) labelService.count());
		
		Assert.assertEquals(label1, labelService.getById("label1"));
		Assert.assertEquals(-1, label1.compareTo(label2));
		
		labelService.delete(label2);
		
		Assert.assertTrue(labelService.count() == 1);
	}
	
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
