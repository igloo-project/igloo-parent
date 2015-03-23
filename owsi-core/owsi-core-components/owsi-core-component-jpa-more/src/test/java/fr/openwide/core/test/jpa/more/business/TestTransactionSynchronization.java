package fr.openwide.core.test.jpa.more.business;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.beans.factory.annotation.Autowired;

import fr.openwide.core.jpa.exception.SecurityServiceException;
import fr.openwide.core.jpa.exception.ServiceException;
import fr.openwide.core.jpa.more.util.transaction.service.TransactionSynchronizationTaskManagerServiceImpl;
import fr.openwide.core.test.jpa.more.business.entity.model.TestEntity;
import fr.openwide.core.test.jpa.more.business.util.transaction.service.ITestTransactionSynchronizationTaskCreatorService;

public class TestTransactionSynchronization extends AbstractJpaMoreTestCase {

	@Autowired
	private ITestTransactionSynchronizationTaskCreatorService testTransactionSynchronizationTaskCreatorService;

	@Rule
	public ExpectedException exception = ExpectedException.none();

	@Ignore
	@Test
	public void testTaskTransactionReadOnly() {
		testTransactionSynchronizationTaskCreatorService.addBasicTaskTransactionReadOnly();
	}

	@Test
	public void testTaskNoTransaction() {
		exception.expect(IllegalStateException.class);
		exception.expectMessage(TransactionSynchronizationTaskManagerServiceImpl.EXCEPTION_MESSAGE_NO_ACTUAL_TRANSACTION_ACTIVE);
		
		// si pas de transaction, impossible de pousser des tasks
		testTransactionSynchronizationTaskCreatorService.addBasicTaskNoTransaction();
	}

	@Ignore
	@Test
	public void testRollbackTask() throws ServiceException, SecurityServiceException {
		// création + commit de l'entité
		TestEntity entity = new TestEntity("entity");
		testEntityService.create(entity);
		
		Long testEntityId = entity.getId();
		
		// ajout d'une task qui supprime l'entité en rollback + rollback effectif
		testTransactionSynchronizationTaskCreatorService.addRollbackBasicTask(testEntityId);
		
		Assert.assertNull(testEntityService.getById(testEntityId));
	}
}
