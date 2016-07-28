package fr.openwide.core.test.jpa.batch;

import static org.hamcrest.CoreMatchers.everyItem;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
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
import org.junit.Before;
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
import fr.openwide.core.jpa.batch.executor.BatchExecutorCreator;
import fr.openwide.core.jpa.batch.executor.MultithreadedBatchExecutor;
import fr.openwide.core.jpa.batch.executor.SimpleHibernateBatchExecutor;
import fr.openwide.core.jpa.batch.runnable.ReadOnlyBatchRunnable;
import fr.openwide.core.jpa.batch.runnable.ReadWriteBatchRunnable;
import fr.openwide.core.jpa.exception.SecurityServiceException;
import fr.openwide.core.jpa.exception.ServiceException;
import fr.openwide.core.jpa.query.IQuery;
import fr.openwide.core.jpa.query.Queries;
import fr.openwide.core.test.AbstractJpaCoreTestCase;
import fr.openwide.core.test.business.person.model.Person;
import fr.openwide.core.test.business.person.model.QPerson;

public class TestBatchExecutorCreator extends AbstractJpaCoreTestCase {

	private static final Logger LOGGER = LoggerFactory.getLogger(TestBatchExecutorCreator.class);

	private static final String NEW_LASTNAME_VALUE = "NEW_LASTNAME_VALUE";

	@Autowired
	private BatchExecutorCreator executorCreator;

	protected TransactionTemplate writeRequiredTransactionTemplate;

	private List<Long> personIds;
	
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

	private void assertNoPersonNamed(String shouldNotBeSetValue) {
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

	private void assertAllPersonsNamed(String shouldBeSetValue) {
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
	
	@Test
	public void testSimpleHibernateBatchReadWrite() {
		List<Long> toExecute = Lists.newArrayList(personIds);
		
		final List<Long> executed = Lists.newArrayList();
		
		SimpleHibernateBatchExecutor executor = executorCreator.newSimpleHibernateBatchExecutor();
		executor.batchSize(10).flushToIndexes(true).reindexClasses(Person.class);
		executor.run(Person.class, toExecute, new ReadWriteBatchRunnable<Person>() {
			@Override
			public void executeUnit(Person unit) {
				LOGGER.warn("Executing: " + unit.getDisplayName());
				unit.setLastName(NEW_LASTNAME_VALUE);
				try {
					personService.update(unit);
					executed.add(unit.getId());
				} catch (ServiceException | SecurityServiceException e) {
					throw new IllegalStateException(e);
				}
			}
		});

		assertEquals(toExecute, executed);
		assertAllPersonsNamed(NEW_LASTNAME_VALUE);
	}
	
	@Test
	public void testSimpleHibernateBatchReadOnly() {
		List<Long> toExecute = Lists.newArrayList(personIds);
		
		SimpleHibernateBatchExecutor executor = executorCreator.newSimpleHibernateBatchExecutor();
		executor.batchSize(10).flushToIndexes(true).reindexClasses(Person.class);

		executor.run(Person.class, toExecute, new ReadOnlyBatchRunnable<Person>() {
			@Override
			public void executeUnit(Person unit) {
				LOGGER.warn("Executing: " + unit.getDisplayName());
				unit.setLastName(NEW_LASTNAME_VALUE);
				try {
					personService.update(unit);
				} catch (ServiceException | SecurityServiceException e) {
					throw new IllegalStateException(e);
				}
			}
		});
		
		assertNoPersonNamed(NEW_LASTNAME_VALUE);
	}

	@Test
	public void testSimpleHibernateBatchCustomNonConsumingQuery() {
		List<Long> toExecute = Lists.newArrayList(Iterables.skip(personIds, 50));
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
		executor.runNonConsuming("Person query", query, new ReadOnlyBatchRunnable<Person>() {
			@Override
			public void executeUnit(Person unit) {
				LOGGER.warn("Executing: " + unit.getDisplayName());
				executed.add(unit.getId());
			}
		});
		
		assertEquals(expectedExecuted, executed);
	}
	
	@Test
	public void testSimpleHibernateBatchCustomConsumingQuery() {
		List<Long> toExecute = Lists.newArrayList(personIds);
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
		executor.runConsuming("Person query", query, new ReadWriteBatchRunnable<Person>() {
			@Override
			public void executeUnit(Person unit) {
				LOGGER.warn("Executing: " + unit.getDisplayName());
				
				/* Remove the "Lastname" prefix, which "consumes" this element
				 * (e.g. it removes this element from the query's results)
				 */
				unit.setLastName(NEW_LASTNAME_VALUE);
				try {
					personService.update(unit);
					executed.add(unit.getId());
				} catch (ServiceException | SecurityServiceException e) {
					throw new IllegalStateException(e);
				}
			}
		});
		
		assertEquals(expectedExecuted, executed);
	}
	
	@Test
	public void testMultithreadedBatchReadWrite() {
		List<Long> toExecute = Lists.newArrayList(personIds);
		Collections.sort(toExecute);
		
		final List<Long> executed = Collections.synchronizedList(Lists.<Long>newArrayList());
		
		MultithreadedBatchExecutor executor = executorCreator.newMultithreadedBatchExecutor();
		executor.batchSize(10).threads(4);
		executor.run(Person.class.getSimpleName(), personIds, new ReadWriteBatchRunnable<Long>() {
			@Override
			public void preExecutePartition(List<Long> partition) {
				LOGGER.warn("Executing partition: " + Joiners.onComma().join(partition));
			}
			
			@Override
			public void executeUnit(Long unit) {
				Person person = personService.getById(unit);
				person.setLastName(NEW_LASTNAME_VALUE);
				try {
					personService.update(person);
					executed.add(person.getId());
				} catch (ServiceException | SecurityServiceException e) {
					throw new IllegalStateException(e);
				}
			}
		});
		
		Collections.sort(executed);
		assertEquals(toExecute, executed);
		assertAllPersonsNamed(NEW_LASTNAME_VALUE);
	}

	@Test
	public void testMultithreadedBatchReadOnly() {
		List<Long> toExecute = Lists.newArrayList(personIds);
		Collections.sort(toExecute);
		
		MultithreadedBatchExecutor executor = executorCreator.newMultithreadedBatchExecutor();
		executor.batchSize(10).threads(4);

		executor.run(Person.class.getSimpleName(), personIds, new ReadOnlyBatchRunnable<Long>() {
			@Override
			public void preExecutePartition(List<Long> partition) {
				LOGGER.warn("Executing partition: " + Joiners.onComma().join(partition));
			}
			
			@Override
			public void executeUnit(Long unit) {
				Person person = personService.getById(unit);
				person.setLastName(NEW_LASTNAME_VALUE);
				try {
					personService.update(person);
				} catch (ServiceException | SecurityServiceException e) {
					throw new IllegalStateException(e);
				}
			}
		});
		
		assertNoPersonNamed(NEW_LASTNAME_VALUE);
	}
	
	@Test
	public void testMultithreadedBatchPostExecute() {
		final MutableBoolean postExecuteWasRun = new MutableBoolean(false);
		writeRequiredTransactionTemplate.execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus status) {
				
				MultithreadedBatchExecutor executor = executorCreator.newMultithreadedBatchExecutor();
				executor.batchSize(10).threads(4);
				executor.run(Person.class.getSimpleName(), personIds, new ReadWriteBatchRunnable<Long>() {
					@Override
					public void preExecutePartition(List<Long> partition) {
						LOGGER.warn("Executing partition: " + Joiners.onComma().join(partition));
					}
					
					@Override
					public void executeUnit(Long unit) {
						Person person = personService.getById(unit);
						person.setLastName(NEW_LASTNAME_VALUE);
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
						Person person = personService.getById(personIds.get(0));
						assertEquals("An entity had not been updated from the postExecute() perspective.",
								person.getLastName(), NEW_LASTNAME_VALUE);
					}
				});
			}
		});
		
		assertTrue("postExecute was not run.", postExecuteWasRun.booleanValue());
	}
	
	private static class PartitionCountingRunnable<T> extends ReadWriteBatchRunnable<T> {
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
	
	private static class PreExecuteFailingRunnable<T> extends PartitionCountingRunnable<T> {
		@Override
		public void preExecute() {
			super.preExecute();
			throw new TestBatchException1();
		}
	}
	
	private static class SequentialFailingRunnable<T> extends PartitionCountingRunnable<T> {
		@Override
		public void preExecutePartition(List<T> partition) {
			LOGGER.warn("Executing partition: " + Joiners.onComma().join(partition));
		}
		
		@Override
		public void executePartition(List<T> partition, int partitionIndex) {
			super.executePartition(partition, partitionIndex);
			switch (partitionIndex) {
			case 0: // First executePartition: succeed
				LOGGER.warn("executePartition#{}: Succeeding", partitionIndex);
				break;
			case 1: // Second executePartition: fail
				LOGGER.warn("executePartition#{}: Failing", partitionIndex);
				throw new TestBatchException1();
			default: // Should not happen
				Assert.fail();
			}
		}
	}
	
	private static class ConcurrentFailingRunnable<T> extends PartitionCountingRunnable<T> {
		private final Semaphore afterFailureSemaphore = new Semaphore(0);
		private final Semaphore beforeFailureSemaphore = new Semaphore(0);
		
		@Override
		public void executePartition(List<T> partition, int partitionIndex) {
			try {
				switch (partitionIndex) {
				case 0: // First executePartition: succeed
					LOGGER.warn("executePartition#{}: Succeeding", partitionIndex);
					break;
				case 1: // Second executePartition: wait for third task to start, then fail
					LOGGER.warn("executePartition#{}: Waiting for next failing task to start", partitionIndex);
					beforeFailureSemaphore.acquire();
					LOGGER.warn("executePartition#{}: Failing", partitionIndex);
					afterFailureSemaphore.release();
					throw new TestBatchException1();
				case 2: // Third executePartition: just fail
					LOGGER.warn("executePartition#{}: Allowing previous task to fail", partitionIndex);
					beforeFailureSemaphore.release();
					LOGGER.warn("executePartition#{}: Failing", partitionIndex);
					afterFailureSemaphore.release();
					throw new TestBatchException1();
				case 3:// Fourth executePartition: wait for failure, then last long enough for the failing thread to shut down
					LOGGER.warn("executePartition#{}: Waiting for another task's failure...", partitionIndex);
					afterFailureSemaphore.acquire();
					LOGGER.warn("executePartition#{}: Another task failed. Succeeding.", partitionIndex);
					Thread.sleep(1000);
					break;
				default: // Other executePartition's: just make sure there will still be tasks pending upon shutdown
					Thread.sleep(2000);
					LOGGER.warn("executePartition#{}: Succeeding (after a short wait)", partitionIndex);
					break;
				}
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				throw new IllegalStateException(e);
			}
		}
	}
	
	@Test
	public void testSimpleHibernateBatchPreExecuteErrorDefaultBehavior() {
		SimpleHibernateBatchExecutor executor = executorCreator.newSimpleHibernateBatchExecutor();
		executor.batchSize(10).flushToIndexes(true).reindexClasses(Person.class);
		
		Exception runException = null;
		PreExecuteFailingRunnable<Person> runnable = new PreExecuteFailingRunnable<>();
		try {
			executor.run(Person.class, personIds, runnable);
		} catch (Exception e) {
			runException = e;
		}

		assertThat(runException, instanceOf(IllegalStateException.class));
		assertThat(runException.getCause(), instanceOf(ExecutionException.class));
		assertThat(runException.getCause().getCause(), instanceOf(TestBatchException1.class));
		assertEquals(0, runnable.getExecutedPartitionCount());
	}
	
	@Test
	public void testSimpleHibernateBatchPreExecuteErrorCustomBehavior() {
		SimpleHibernateBatchExecutor executor = executorCreator.newSimpleHibernateBatchExecutor();
		executor.batchSize(10).flushToIndexes(true).reindexClasses(Person.class);
		
		Exception runException = null;
		PreExecuteFailingRunnable<Person> runnable = new PreExecuteFailingRunnable<Person>() {
			@Override
			public void onError(ExecutionException exception) {
				throw new TestBatchException2(exception);
			}
		};
		try {
			executor.run(Person.class, personIds, runnable);
		} catch (Exception e) {
			runException = e;
		}
		
		assertThat(runException, instanceOf(TestBatchException2.class));
		assertThat(runException.getCause(), instanceOf(ExecutionException.class));
		assertThat(runException.getCause().getCause(), instanceOf(TestBatchException1.class));
		assertEquals(0, runnable.getExecutedPartitionCount());
	}
	
	@Test
	public void testSimpleHibernateBatchExecutePartitionErrorDefaultBehavior() {
		SimpleHibernateBatchExecutor executor = executorCreator.newSimpleHibernateBatchExecutor();
		executor.batchSize(10).flushToIndexes(true).reindexClasses(Person.class);
		
		Exception runException = null;
		SequentialFailingRunnable<Person> runnable = new SequentialFailingRunnable<>();
		try {
			executor.run(Person.class, personIds, runnable);
		} catch (Exception e) {
			runException = e;
		}

		assertThat(runException, instanceOf(IllegalStateException.class));
		assertThat(runException.getCause(), instanceOf(ExecutionException.class));
		assertThat(runException.getCause().getCause(), instanceOf(TestBatchException1.class));
		assertEquals(2, runnable.getExecutedPartitionCount());
	}
	
	@Test
	public void testSimpleHibernateBatchExecutePartitionErrorCustomBehavior() {
		SimpleHibernateBatchExecutor executor = executorCreator.newSimpleHibernateBatchExecutor();
		executor.batchSize(10).flushToIndexes(true).reindexClasses(Person.class);
		
		Exception runException = null;
		SequentialFailingRunnable<Person> runnable = new SequentialFailingRunnable<Person>() {
			@Override
			public void onError(ExecutionException exception) {
				throw new TestBatchException2(exception);
			}
		};
		try {
			executor.run(Person.class, personIds, runnable);
		} catch (Exception e) {
			runException = e;
		}
		
		assertThat(runException, instanceOf(TestBatchException2.class));
		assertThat(runException.getCause(), instanceOf(ExecutionException.class));
		assertThat(runException.getCause().getCause(), instanceOf(TestBatchException1.class));
		assertEquals(2, runnable.getExecutedPartitionCount());
	}
	
	@Test
	public void testMultithreadedBatchPreExecuteErrorDefaultBehavior() {
		MultithreadedBatchExecutor executor = executorCreator.newMultithreadedBatchExecutor();
		executor.batchSize(10).threads(4);

		Exception runException = null;
		PreExecuteFailingRunnable<Long> runnable = new PreExecuteFailingRunnable<Long>();
		try {
			executor.run("FailingProcess", personIds, runnable);
		} catch (Exception e) {
			runException = e;
		}
		
		assertThat(runException, instanceOf(IllegalStateException.class));
		assertThat(runException.getCause(), instanceOf(ExecutionException.class));
		assertThat(runException.getCause().getCause(), instanceOf(TestBatchException1.class));
		assertEquals(0, runException.getCause().getSuppressed().length);
		assertEquals(0, runnable.getExecutedPartitionCount());
	}
	
	@Test
	public void testMultithreadedBatchPreExecuteErrorCustomException() {
		MultithreadedBatchExecutor executor = executorCreator.newMultithreadedBatchExecutor();
		executor.batchSize(10).threads(4);

		Exception runException = null;
		PreExecuteFailingRunnable<Long> runnable = new PreExecuteFailingRunnable<Long>() {
			@Override
			public void onError(ExecutionException exception) {
				throw new TestBatchException2(exception);
			}
		};
		try {
			executor.run("FailingProcess", personIds, runnable);
		} catch (Exception e) {
			runException = e;
		}
		assertThat(runException, instanceOf(TestBatchException2.class));
		assertThat(runException.getCause(), instanceOf(ExecutionException.class));
		assertThat(runException.getCause().getCause(), instanceOf(TestBatchException1.class));
		assertEquals(0, runException.getCause().getSuppressed().length);
		assertEquals(0, runnable.getExecutedPartitionCount());
	}
	
	@Test
	public void testMultithreadedBatchExecutePartitionErrorDefaultBehavior() {
		MultithreadedBatchExecutor executor = executorCreator.newMultithreadedBatchExecutor();
		executor.batchSize(10).threads(4);

		Exception runException = null;
		ConcurrentFailingRunnable<Long> runnable = new ConcurrentFailingRunnable<Long>(); // Requires at least 2 threads
		try {
			executor.run("FailingProcess", personIds, runnable);
		} catch (Exception e) {
			runException = e;
		}
		
		assertThat(runException, instanceOf(IllegalStateException.class));
		assertThat(runException.getCause(), instanceOf(ExecutionException.class));
		assertThat(runException.getCause().getCause(), instanceOf(TestBatchException1.class));
		assertEquals(1, runException.getCause().getSuppressed().length);
		assertThat(Arrays.asList(runException.getCause().getSuppressed()),
				everyItem(CoreMatchers.<Throwable>instanceOf(TestBatchException1.class)));
		assertTrue("The executor did not abort as expected", 10 > runnable.getExecutedPartitionCount());
	}
	
	@Test
	public void testMultithreadedBatchExecutePartitionErrorCustomException() {
		MultithreadedBatchExecutor executor = executorCreator.newMultithreadedBatchExecutor();
		executor.batchSize(10).threads(4);

		Exception runException = null;
		ConcurrentFailingRunnable<Long> runnable = new ConcurrentFailingRunnable<Long>() { // Requires at least 2 threads
			@Override
			public void onError(ExecutionException exception) {
				throw new TestBatchException2(exception);
			}
		};
		try {
			executor.run("FailingProcess", personIds, runnable);
		} catch (Exception e) {
			runException = e;
		}
		assertThat(runException, instanceOf(TestBatchException2.class));
		assertThat(runException.getCause(), instanceOf(ExecutionException.class));
		assertThat(runException.getCause().getCause(), instanceOf(TestBatchException1.class));
		assertEquals(1, runException.getCause().getSuppressed().length);
		assertThat(Arrays.asList(runException.getCause().getSuppressed()),
				everyItem(CoreMatchers.<Throwable>instanceOf(TestBatchException1.class)));
		assertTrue("The executor did not abort as expected", 10 > runnable.getExecutedPartitionCount());
	}
	
	@Test
	public void testMultithreadedBatchExecutePartitionErrorNoAbort() {
		MultithreadedBatchExecutor executor = executorCreator.newMultithreadedBatchExecutor();
		executor.batchSize(10).threads(4);
		executor.abortAllOnExecutionError(false);

		Exception runException = null;
		ConcurrentFailingRunnable<Long> runnable = new ConcurrentFailingRunnable<Long>(); // Requires at least 2 threads
		try {
			executor.run("FailingProcess", personIds, runnable);
		} catch (Exception e) {
			runException = e;
		}
		assertThat(runException, instanceOf(IllegalStateException.class));
		assertThat(runException.getCause(), instanceOf(ExecutionException.class));
		assertThat(runException.getCause().getCause(), instanceOf(TestBatchException1.class));
		assertEquals(1, runException.getCause().getSuppressed().length);
		assertThat(Arrays.asList(runException.getCause().getSuppressed()),
				everyItem(CoreMatchers.<Throwable>instanceOf(TestBatchException1.class)));
		assertEquals("The executor did abort (this was unexpected)", 10, runnable.getExecutedPartitionCount());
	}
}
