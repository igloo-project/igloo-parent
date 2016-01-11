package fr.openwide.core.test.jpa.batch;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.everyItem;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.lang3.mutable.MutableBoolean;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.interceptor.DefaultTransactionAttribute;
import org.springframework.transaction.interceptor.TransactionAttribute;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;
import com.querydsl.jpa.impl.JPAQuery;

import fr.openwide.core.commons.util.functional.Joiners;
import fr.openwide.core.jpa.batch.executor.AbstractBatchRunnable;
import fr.openwide.core.jpa.batch.executor.BatchExecutorCreator;
import fr.openwide.core.jpa.batch.executor.MultithreadedBatchExecutor;
import fr.openwide.core.jpa.batch.executor.SimpleHibernateBatchExecutor;
import fr.openwide.core.jpa.exception.SecurityServiceException;
import fr.openwide.core.jpa.exception.ServiceException;
import fr.openwide.core.jpa.query.IQuery;
import fr.openwide.core.jpa.query.Queries;
import fr.openwide.core.test.AbstractJpaCoreTestCase;
import fr.openwide.core.test.business.person.model.Person;
import fr.openwide.core.test.business.person.model.QPerson;

public class TestBatchExecutorCreator extends AbstractJpaCoreTestCase {

	private static final Logger LOGGER = LoggerFactory.getLogger(TestBatchExecutorCreator.class);

	@Autowired
	private BatchExecutorCreator executorCreator;

	protected TransactionTemplate writeRequiredTransactionTemplate;
	
	@Autowired
	public void setPlatformTransactionManager(PlatformTransactionManager transactionManager) {
		DefaultTransactionAttribute writeRequiredTransactionAttribute =
				new DefaultTransactionAttribute(TransactionAttribute.PROPAGATION_REQUIRED);
		writeRequiredTransactionAttribute.setReadOnly(false);
		writeRequiredTransactionTemplate =
				new TransactionTemplate(transactionManager, writeRequiredTransactionAttribute);
	}
	
	@Test
	public void testSimpleHibernateBatch() throws ServiceException, SecurityServiceException {
		List<Long> ids = Lists.newArrayList();
		
		for (int i = 1; i < 100; i++) {
			Person person = new Person("Firstname" + i, "Lastname" + i);
			personService.create(person);
			ids.add(person.getId());
		}
		
		List<Long> toExecute = Lists.newArrayList(ids);
		
		final List<Long> executed = Lists.newArrayList();
		
		SimpleHibernateBatchExecutor executor = executorCreator.newSimpleHibernateBatchExecutor();
		executor.batchSize(10).flushToIndexes(true).reindexClasses(Person.class);
		executor.run(Person.class, toExecute, new AbstractBatchRunnable<Person>() {
			@Override
			public void executeUnit(Person unit) {
				LOGGER.warn("Executing: " + unit.getDisplayName());
				executed.add(unit.getId());
			}
		});
		
		assertEquals(toExecute, executed);
	}
	
	@Test
	public void testSimpleHibernateBatchCustomNonConsumingQuery() throws ServiceException, SecurityServiceException {
		List<Long> ids = Lists.newArrayList();
		
		for (int i = 1; i < 100; i++) {
			Person person = new Person("Firstname" + i, "Lastname" + i);
			personService.create(person);
			ids.add(person.getId());
		}
		
		List<Long> toExecute = Lists.newArrayList(Iterables.skip(ids, 50));
		Collections.sort(toExecute);
		
		IQuery<Person> query = Queries.fromQueryDsl(
				new JPAQuery<Person>(getEntityManager())
				.from(QPerson.person)
				.where(QPerson.person.id.in(toExecute))
				.orderBy(QPerson.person.id.desc())
		);
		List<Long> expectedExecuted = Lists.newArrayList(toExecute);
		Collections.sort(expectedExecuted, Ordering.natural().reverse());
		
		final List<Long> executed = Lists.newArrayList();
		
		SimpleHibernateBatchExecutor executor = executorCreator.newSimpleHibernateBatchExecutor();
		executor.batchSize(10).flushToIndexes(true).reindexClasses(Person.class);
		executor.runNonConsuming("Person query", query, new AbstractBatchRunnable<Person>() {
			@Override
			public void executeUnit(Person unit) {
				LOGGER.warn("Executing: " + unit.getDisplayName());
				executed.add(unit.getId());
			}
		});
		
		assertEquals(expectedExecuted, executed);
	}
	
	@Test
	public void testSimpleHibernateBatchCustomConsumingQuery() throws ServiceException, SecurityServiceException {
		List<Long> ids = Lists.newArrayList();
		
		for (int i = 1; i < 100; i++) {
			Person person = new Person("Firstname" + i, "Lastname" + i);
			personService.create(person);
			ids.add(person.getId());
		}
		
		List<Long> toExecute = Lists.newArrayList(ids);
		Collections.sort(toExecute);
		
		IQuery<Person> query = Queries.fromQueryDsl(
				new JPAQuery<Person>(getEntityManager())
				.from(QPerson.person)
				.where(QPerson.person.lastName.like("Lastname%"))
				.orderBy(QPerson.person.id.desc())
		);
		List<Long> expectedExecuted = Lists.newArrayList(toExecute);
		Collections.sort(expectedExecuted, Ordering.natural().reverse());
		
		final List<Long> executed = Lists.newArrayList();
		
		SimpleHibernateBatchExecutor executor = executorCreator.newSimpleHibernateBatchExecutor();
		executor.batchSize(10).flushToIndexes(true).reindexClasses(Person.class);
		executor.runConsuming("Person query", query, new AbstractBatchRunnable<Person>() {
			@Override
			public void executeUnit(Person unit) {
				LOGGER.warn("Executing: " + unit.getDisplayName());
				
				/* Remove the "Lastname " prefix, which "consumes" this element
				 * (e.g. it removes this element from the query's results)
				 */
				unit.setLastName(unit.getLastName().replace("Lastname", ""));
				
				executed.add(unit.getId());
			}
		});
		
		assertEquals(expectedExecuted, executed);
	}
	
	
	@Test
	public void testMultithreadedBatch() throws ServiceException, SecurityServiceException {
		List<Long> ids = Lists.newArrayList();
		
		for (int i = 1; i < 100; i++) {
			Person person = new Person("Firstname" + i, "Lastname" + i);
			personService.create(person);
			ids.add(person.getId());
		}
		
		MultithreadedBatchExecutor executor = executorCreator.newMultithreadedBatchExecutor();
		executor.batchSize(10).threads(4);
		executor.run(Person.class.getSimpleName(), ids, new AbstractBatchRunnable<Long>() {
			@Override
			public void preExecutePartition(List<Long> partition) {
				LOGGER.warn("Executing partition: " + Joiners.onComma().join(partition));
			}
			
			@Override
			public void executeUnit(Long unit) {
				Person person = personService.getById(unit);
				person.setLastName(person.getLastName() + " updated " + person.getId());
				try {
					personService.update(person);
					LOGGER.warn("Updated: " + person.getDisplayName());
				} catch (ServiceException | SecurityServiceException e) {
					throw new IllegalStateException(e);
				}
			}
		});
	}
	
	@Test
	public void testMultithreadedBatchPostExecute() throws ServiceException, SecurityServiceException {
		final List<Long> ids = Lists.newArrayList();
		for (int i = 1; i < 100; i++) {
			Person person = new Person("Firstname" + i, "Lastname" + i);
			personService.create(person);
			ids.add(person.getId());
		}
		
		final MutableBoolean postExecuteWasRun = new MutableBoolean(false);
		writeRequiredTransactionTemplate.execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus status) {
				
				MultithreadedBatchExecutor executor = executorCreator.newMultithreadedBatchExecutor();
				executor.batchSize(10).threads(4);
				executor.run(Person.class.getSimpleName(), ids, new AbstractBatchRunnable<Long>() {
					@Override
					public void preExecutePartition(List<Long> partition) {
						LOGGER.warn("Executing partition: " + Joiners.onComma().join(partition));
					}
					
					@Override
					public void executeUnit(Long unit) {
						Person person = personService.getById(unit);
						person.setLastName(person.getLastName() + " updated " + person.getId());
						try {
							personService.update(person);
							LOGGER.warn("Updated: " + person.getDisplayName());
						} catch (ServiceException | SecurityServiceException e) {
							throw new IllegalStateException(e);
						}
					}
					
					@Override
					public void postExecute() {
						postExecuteWasRun.setTrue();
						Person person = personService.getById(ids.get(0));
						assertThat("An entity had not been updated from the postExecute() perspective.",
								person.getLastName(), containsString(" updated "));
					}
				});
			}
		});
		
		assertTrue("postExecute was not run.", postExecuteWasRun.booleanValue());
	}
	
	private static class SequentialFailingRunnable<T> extends AbstractBatchRunnable<T> {
		private final AtomicInteger taskCount = new AtomicInteger(0);
		
		public int getExecutedTaskCount() {
			return taskCount.get();
		}
		
		@Override
		public void preExecutePartition(List<T> partition) {
			LOGGER.warn("Executing partition: " + Joiners.onComma().join(partition));
		}
		
		@Override
		public void executePartition(List<T> partition) {
			int taskNumber = taskCount.getAndIncrement();
			switch (taskNumber) {
			case 0: // First task: succeed
				LOGGER.warn("Task#{}: Succeeding", taskNumber);
				break;
			case 1: // Second task: fail
				LOGGER.warn("Task#{}: Failing", taskNumber);
				throw new TestBatchException1();
			default: // Should not happen
				Assert.fail();
			}
		}
	}
	
	private static class ConcurrentFailingRunnable<T> extends AbstractBatchRunnable<T> {
		private final Semaphore afterFailureSemaphore = new Semaphore(0);
		private final Semaphore beforeFailureSemaphore = new Semaphore(0);
		private final AtomicInteger taskCount = new AtomicInteger(0);

		public int getExecutedTaskCount() {
			return taskCount.get();
		}
		
		@Override
		public void preExecutePartition(List<T> partition) {
			LOGGER.warn("Executing partition: " + Joiners.onComma().join(partition));
		}
		
		@Override
		public void executePartition(List<T> partition) {
			try {
				int taskNumber = taskCount.getAndIncrement();
				switch (taskNumber) {
				case 0: // First task: succeed
					LOGGER.warn("Task#{}: Succeeding", taskNumber);
					break;
				case 1: // Second task: wait for third task to start, then fail
					LOGGER.warn("Task#{}: Waiting for next failing task to start", taskNumber);
					beforeFailureSemaphore.acquire();
					LOGGER.warn("Task#{}: Failing", taskNumber);
					afterFailureSemaphore.release();
					throw new TestBatchException1();
				case 2: // Third task: just fail
					LOGGER.warn("Task#{}: Allowing previous task to fail", taskNumber);
					beforeFailureSemaphore.release();
					LOGGER.warn("Task#{}: Failing", taskNumber);
					afterFailureSemaphore.release();
					throw new TestBatchException1();
				case 3:// Fourth task: wait for failure, then last long enough for the failing thread to shut down
					LOGGER.warn("Task#{}: Waiting for another task's failure...", taskNumber);
					afterFailureSemaphore.acquire();
					LOGGER.warn("Task#{}: Another task failed. Succeeding.", taskNumber);
					Thread.sleep(1000);
					break;
				default: // Other tasks: just make sure there will still be tasks pending upon shutdown
					Thread.sleep(2000);
					LOGGER.warn("Task#{}: Succeeding (after a short wait)", taskNumber);
					break;
				}
			} catch (InterruptedException e) {
				throw new IllegalStateException(e);
			}
		}
	}
	
	@Test
	public void testSimpleHibernateBatchErrorDefaultBehavior() throws ServiceException, SecurityServiceException {
		List<Long> ids = Lists.newArrayList();
		
		for (int i = 1; i < 100; i++) {
			Person person = new Person("Firstname" + i, "Lastname" + i);
			personService.create(person);
			ids.add(person.getId());
		}
		
		SimpleHibernateBatchExecutor executor = executorCreator.newSimpleHibernateBatchExecutor();
		executor.batchSize(10).flushToIndexes(true).reindexClasses(Person.class);
		
		Exception runException = null;
		SequentialFailingRunnable<Person> runnable = new SequentialFailingRunnable<>();
		try {
			executor.run(Person.class, ids, runnable);
		} catch (Exception e) {
			runException = e;
		}

		assertThat(runException, instanceOf(IllegalStateException.class));
		assertThat(runException.getCause(), instanceOf(TestBatchException1.class));
		assertEquals(2, runnable.getExecutedTaskCount());
	}
	
	@Test
	public void testSimpleHibernateBatchErrorCustomBehavior() throws ServiceException, SecurityServiceException {
		List<Long> ids = Lists.newArrayList();
		
		for (int i = 1; i < 100; i++) {
			Person person = new Person("Firstname" + i, "Lastname" + i);
			personService.create(person);
			ids.add(person.getId());
		}
		
		SimpleHibernateBatchExecutor executor = executorCreator.newSimpleHibernateBatchExecutor();
		executor.batchSize(10).flushToIndexes(true).reindexClasses(Person.class);
		
		Exception runException = null;
		SequentialFailingRunnable<Person> runnable = new SequentialFailingRunnable<Person>() {
			@Override
			public void onError(Exception exception) {
				throw new TestBatchException2(exception);
			}
		};
		try {
			executor.run(Person.class, ids, runnable);
		} catch (Exception e) {
			runException = e;
		}
		
		assertThat(runException, instanceOf(TestBatchException2.class));
		assertThat(runException.getCause(), instanceOf(TestBatchException1.class));
		assertEquals(2, runnable.getExecutedTaskCount());
	}
	
	@Test
	public void testMultithreadedBatchErrorDefaultBehavior() throws ServiceException, SecurityServiceException {
		List<Long> ids = Lists.newArrayList();
		
		for (long i = 1; i < 100; i++) {
			ids.add(i);
		}
		
		MultithreadedBatchExecutor executor = executorCreator.newMultithreadedBatchExecutor();
		executor.batchSize(10).threads(4);

		Exception runException = null;
		ConcurrentFailingRunnable<Long> runnable = new ConcurrentFailingRunnable<Long>(); // Requires at least 2 threads
		try {
			executor.run("FailingProcess", ids, runnable);
		} catch (Exception e) {
			runException = e;
		}
		
		assertThat(runException, instanceOf(IllegalStateException.class));
		assertThat(runException.getCause(), instanceOf(ExecutionException.class));
		assertThat(runException.getCause().getCause(), instanceOf(TestBatchException1.class));
		assertEquals(1, runException.getCause().getSuppressed().length);
		assertThat(Arrays.asList(runException.getCause().getSuppressed()),
				everyItem(CoreMatchers.<Throwable>instanceOf(TestBatchException1.class)));
		assertTrue("The executor did not abort as expected", 10 > runnable.getExecutedTaskCount());
	}
	
	@Test
	public void testMultithreadedBatchErrorCustomException() throws ServiceException, SecurityServiceException {
		List<Long> ids = Lists.newArrayList();
		
		for (long i = 1; i < 100; i++) {
			ids.add(i);
		}
		
		MultithreadedBatchExecutor executor = executorCreator.newMultithreadedBatchExecutor();
		executor.batchSize(10).threads(4);

		Exception runException = null;
		ConcurrentFailingRunnable<Long> runnable = new ConcurrentFailingRunnable<Long>() { // Requires at least 2 threads
			@Override
			public void onError(Exception exception) {
				throw new TestBatchException2(exception);
			}
		};
		try {
			executor.run("FailingProcess", ids, runnable);
		} catch (Exception e) {
			runException = e;
		}
		assertThat(runException, instanceOf(TestBatchException2.class));
		assertThat(runException.getCause(), instanceOf(ExecutionException.class));
		assertThat(runException.getCause().getCause(), instanceOf(TestBatchException1.class));
		assertEquals(1, runException.getCause().getSuppressed().length);
		assertThat(Arrays.asList(runException.getCause().getSuppressed()),
				everyItem(CoreMatchers.<Throwable>instanceOf(TestBatchException1.class)));
		assertTrue("The executor did not abort as expected", 10 > runnable.getExecutedTaskCount());
	}
	
	@Test
	public void testMultithreadedBatchErrorNoAbort() throws ServiceException, SecurityServiceException {
		List<Long> ids = Lists.newArrayList();
		
		for (long i = 1; i < 100; i++) {
			ids.add(i);
		}
		
		MultithreadedBatchExecutor executor = executorCreator.newMultithreadedBatchExecutor();
		executor.batchSize(10).threads(4);
		executor.abortAllOnExecutionError(false);

		Exception runException = null;
		ConcurrentFailingRunnable<Long> runnable = new ConcurrentFailingRunnable<Long>(); // Requires at least 2 threads
		try {
			executor.run("FailingProcess", ids, runnable);
		} catch (Exception e) {
			runException = e;
		}
		assertThat(runException, instanceOf(IllegalStateException.class));
		assertThat(runException.getCause(), instanceOf(ExecutionException.class));
		assertThat(runException.getCause().getCause(), instanceOf(TestBatchException1.class));
		assertEquals(1, runException.getCause().getSuppressed().length);
		assertThat(Arrays.asList(runException.getCause().getSuppressed()),
				everyItem(CoreMatchers.<Throwable>instanceOf(TestBatchException1.class)));
		assertEquals("The executor did abort (this was unexpected)", 10, runnable.getExecutedTaskCount());
	}
}
