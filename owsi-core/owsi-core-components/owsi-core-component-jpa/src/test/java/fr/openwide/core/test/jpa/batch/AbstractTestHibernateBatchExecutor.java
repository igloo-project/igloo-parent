package fr.openwide.core.test.jpa.batch;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.interceptor.DefaultTransactionAttribute;
import org.springframework.transaction.interceptor.TransactionAttribute;
import org.springframework.transaction.support.TransactionTemplate;

import com.google.common.collect.Lists;
import com.querydsl.jpa.impl.JPAQuery;

import fr.openwide.core.commons.util.functional.Joiners;
import fr.openwide.core.jpa.batch.executor.BatchExecutorCreator;
import fr.openwide.core.jpa.batch.runnable.ReadWriteBatchRunnable;
import fr.openwide.core.jpa.exception.SecurityServiceException;
import fr.openwide.core.jpa.exception.ServiceException;
import fr.openwide.core.test.AbstractJpaCoreTestCase;
import fr.openwide.core.test.business.person.model.Person;
import fr.openwide.core.test.business.person.model.QPerson;

public abstract class AbstractTestHibernateBatchExecutor extends AbstractJpaCoreTestCase {

	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractTestHibernateBatchExecutor.class);

	protected static final String NEW_LASTNAME_VALUE = "NEW_LASTNAME_VALUE";

	@Autowired
	private BatchExecutorCreator executorCreator;

	protected List<Long> personIds;

	protected TransactionTemplate writeRequiredTransactionTemplate;

	@Autowired
	public void setPlatformTransactionManager(PlatformTransactionManager transactionManager) {
		DefaultTransactionAttribute writeRequiredTransactionAttribute =
				new DefaultTransactionAttribute(TransactionAttribute.PROPAGATION_REQUIRED);
		writeRequiredTransactionAttribute.setReadOnly(false);
		writeRequiredTransactionTemplate =
				new TransactionTemplate(transactionManager, writeRequiredTransactionAttribute);
	}
	
	@Before
	public void initPersons() throws ServiceException, SecurityServiceException {
		personIds = Lists.newArrayList();
		
		for (int i = 1; i < 100; i++) {
			Person person = new Person("Firstname" + i, "Lastname" + i);
			personService.create(person);
			personIds.add(person.getId());
		}
		getEntityManager().clear();
	}

	protected void assertNoPersonNamed(String shouldNotBeSetValue) {
		getEntityManager().clear();
		List<Person> persons = new JPAQuery<Person>(getEntityManager())
				.from(QPerson.person)
				.orderBy(QPerson.person.id.desc())
				.fetch();
		for (Person person : persons) {
			assertNotEquals(
					String.format("%s had the wrong lastname", person),
					shouldNotBeSetValue, person.getLastName()
			);
		}
	}

	protected void assertAllPersonsNamed(String shouldBeSetValue) {
		getEntityManager().clear();
		List<Person> persons = new JPAQuery<Person>(getEntityManager())
				.from(QPerson.person)
				.orderBy(QPerson.person.id.desc())
				.fetch();
		for (Person person : persons) {
			assertEquals(
					String.format("%s had the wrong lastname", person),
					shouldBeSetValue, person.getLastName()
			);
		}
	}
	
	protected static class PartitionCountingRunnable<T> extends ReadWriteBatchRunnable<T> {
		private final AtomicInteger executedPartitionCount = new AtomicInteger(0);
		
		public int getExecutedPartitionCount() {
			return executedPartitionCount.get();
		}
		
		@Override
		public void preExecutePartition(List<T> partition) {
			LOGGER.warn("Executing partition: " + Joiners.onComma().join(partition));
		}
		
		@Override
		public final void executePartition(List<T> partition) {
			int partitionIndex = executedPartitionCount.getAndIncrement();
			executePartition(partition, partitionIndex);
		}
		
		public void executePartition(List<T> partition, int partitionIndex) {
			super.executePartition(partition);
		}
	}
	
	protected static class PreExecuteFailingRunnable<T> extends PartitionCountingRunnable<T> {
		@Override
		public void preExecute() {
			super.preExecute();
			throw new TestBatchException1();
		}
	}
	
}
