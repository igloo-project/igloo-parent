package org.iglooproject.test.jpa.batch;

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

import org.apache.commons.lang3.mutable.MutableBoolean;
import org.hamcrest.CoreMatchers;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;

import com.google.common.collect.Lists;

import org.iglooproject.functional.Joiners;
import org.iglooproject.jpa.batch.executor.BatchExecutorCreator;
import org.iglooproject.jpa.batch.executor.MultithreadedBatchExecutor;
import org.iglooproject.jpa.batch.runnable.ReadOnlyBatchRunnable;
import org.iglooproject.jpa.batch.runnable.ReadWriteBatchRunnable;
import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.iglooproject.test.business.person.model.Person;

public class TestMultithreadedBatchExecutor extends AbstractTestHibernateBatchExecutor {

	private static final Logger LOGGER = LoggerFactory.getLogger(TestMultithreadedBatchExecutor.class);

	@Autowired
	private BatchExecutorCreator executorCreator;
	
	@Test
	public void readWrite() {
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
	public void readOnly() {
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
	public void postExecute() {
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
							LOGGER.warn("Updated: " + person);
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
	public void preExecuteErrorDefaultBehavior() {
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
	public void preExecuteErrorCustomException() {
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
	public void executePartitionErrorDefaultBehavior() {
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
	public void executePartitionErrorCustomException() {
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
	public void executePartitionErrorNoAbort() {
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
