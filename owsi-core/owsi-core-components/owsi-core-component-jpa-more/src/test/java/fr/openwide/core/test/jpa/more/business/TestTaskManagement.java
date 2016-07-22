package fr.openwide.core.test.jpa.more.business;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.Serializable;
import java.util.Date;
import java.util.concurrent.ConcurrentMap;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.common.base.Supplier;
import com.google.common.collect.Maps;

import fr.openwide.core.jpa.business.generic.service.IEntityService;
import fr.openwide.core.jpa.exception.SecurityServiceException;
import fr.openwide.core.jpa.exception.ServiceException;
import fr.openwide.core.jpa.more.business.task.model.AbstractTask;
import fr.openwide.core.jpa.more.business.task.model.QueuedTaskHolder;
import fr.openwide.core.jpa.more.business.task.model.TaskExecutionResult;
import fr.openwide.core.jpa.more.business.task.service.IQueuedTaskHolderManager;
import fr.openwide.core.jpa.more.business.task.service.IQueuedTaskHolderService;
import fr.openwide.core.jpa.more.business.task.util.TaskResult;
import fr.openwide.core.jpa.more.business.task.util.TaskStatus;
import fr.openwide.core.test.jpa.more.business.task.config.TestTaskManagementConfig;

@ContextConfiguration(classes = TestTaskManagementConfig.class)
public class TestTaskManagement extends AbstractJpaMoreTestCase {
	
	@Autowired
	private IEntityService entityService;
	
	@Autowired
	private IQueuedTaskHolderManager manager;
	
	@Autowired
	private IQueuedTaskHolderService taskHolderService;

	private TransactionTemplate transactionTemplate;
	
	/**
	 * A utility used to check that a given task has been correctly executed.
	 * <p>Designed to always reference the same value, even having been serialized with Jackson.
	 */
	protected static class StaticValueAccessor<T> implements Supplier<T>, Serializable {
		private static final long serialVersionUID = 1L;
		
		protected static final ConcurrentMap<Integer, Object> values = Maps.newConcurrentMap();
		protected static volatile int idCounter = 0;
		
		private int id;
		
		public StaticValueAccessor() {
			this.id = ++idCounter;
		}
		
		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		@Override
		@SuppressWarnings("unchecked")
		public T get() {
			return (T) values.get(id); 
		}

		public void set(T value) {
			values.put(id, value);
		}
	}
	
	private static abstract class AbstractTestTask extends AbstractTask {
		private static final long serialVersionUID = 1L;
		public AbstractTestTask() {
			super("task", "type", new Date());
		}
	}

	public static class SimpleTestTask<T> extends AbstractTestTask {
		private static final long serialVersionUID = 1L;
		
		private StaticValueAccessor<T> valueAccessor;

		private T expectedValue;

		@JsonIgnoreProperties("stackTrace")
		private TaskExecutionResult expectedResult;
		
		private int timeToWaitMs = 0;
		
		protected SimpleTestTask() {
		}
		
		public SimpleTestTask(StaticValueAccessor<T> valueAccessor, T expectedValue, TaskExecutionResult expectedResult) {
			super();
			this.valueAccessor = valueAccessor;
			this.expectedValue = expectedValue;
			this.expectedResult = expectedResult;
		}
		
		@Override
		protected TaskExecutionResult doTask() throws Exception {
			if (timeToWaitMs != 0) {
				Thread.sleep(0);
			}
			valueAccessor.set(expectedValue);
			return expectedResult;
		}

		public StaticValueAccessor<T> getValueAccessor() {
			return valueAccessor;
		}

		public void setValueAccessor(StaticValueAccessor<T> valueAccessor) {
			this.valueAccessor = valueAccessor;
		}

		public T getExpectedValue() {
			return expectedValue;
		}

		public void setExpectedValue(T expectedValue) {
			this.expectedValue = expectedValue;
		}

		public TaskExecutionResult getExpectedResult() {
			return expectedResult;
		}

		public void setExpectedResult(TaskExecutionResult expectedResult) {
			this.expectedResult = expectedResult;
		}

		protected int getTimeToWaitMs() {
			return timeToWaitMs;
		}

		protected void setTimeToWaitMs(int timeToWaitMs) {
			this.timeToWaitMs = timeToWaitMs;
		}
	}

	@Autowired
	public void setTransactionManager(PlatformTransactionManager transactionManager) {
		transactionTemplate = new TransactionTemplate(transactionManager);
	}

	@Before
	public void setup() throws ServiceException, SecurityServiceException {
		manager.start();
	}

	@Override
	protected void cleanAll() throws ServiceException, SecurityServiceException {
		cleanEntities(taskHolderService);
		super.cleanAll();
	}
	
	@Test
	public void simple() throws Exception {
		final StaticValueAccessor<String> result = new StaticValueAccessor<>();
		final StaticValueAccessor<Long> taskHolderId = new StaticValueAccessor<>();
		transactionTemplate.execute(new TransactionCallbackWithoutResult() {
			@Override
			public void doInTransactionWithoutResult(TransactionStatus status) {
				try {
					QueuedTaskHolder taskHolder = manager.submit(
							new SimpleTestTask<>(result, "success", TaskExecutionResult.completed())
					);
					taskHolderId.set(taskHolder.getId());
				} catch (ServiceException e) {
					throw new IllegalStateException(e);
				}
			}
		});

		entityService.flush();
		entityService.clear();
		
		// Wait for the task to be consumed
		Thread.sleep(10000);
		
		QueuedTaskHolder taskHolder = taskHolderService.getById(taskHolderId.get());
		assertEquals(TaskStatus.COMPLETED, taskHolder.getStatus());
		assertEquals(TaskResult.SUCCESS, taskHolder.getResult());
		assertEquals("success", result.get());
	}
	
	@Test
	public void noTransaction() throws Exception {
		final StaticValueAccessor<String> result = new StaticValueAccessor<>();
		QueuedTaskHolder taskHolder = manager.submit(
				new SimpleTestTask<>(result, "success", TaskExecutionResult.completed())
		);

		entityService.flush();
		entityService.clear();
		
		// Wait for the task to be consumed
		Thread.sleep(10000);
		
		taskHolder = taskHolderService.getById(taskHolder.getId());
		assertEquals(TaskStatus.COMPLETED, taskHolder.getStatus());
		assertEquals(TaskResult.SUCCESS, taskHolder.getResult());
		assertEquals("success", result.get());
	}
	
	@Test
	public void submitInLongTransaction() throws Exception {
		final StaticValueAccessor<String> result = new StaticValueAccessor<>();
		final StaticValueAccessor<Long> taskHolderId = new StaticValueAccessor<>();
		transactionTemplate.execute(new TransactionCallbackWithoutResult() {
			@Override
			public void doInTransactionWithoutResult(TransactionStatus status) {
				try {
					QueuedTaskHolder taskHolder = manager.submit(
							new SimpleTestTask<>(result, "success", TaskExecutionResult.completed())
					);
					
					taskHolderId.set(taskHolder.getId());
					
					/*
					 * The old implementation waited 2+3×10+2+2×10+2+1×10 = 66s for the TaskHolder to be persisted
					 * to the database, and then simply aborted.
					 * This checks that there will be no such issue anymore.
					 */
					Thread.sleep(70000);
					
					// Check that the task has not been consumed during this transaction (which could be aborted)
					assertNull(result.get());
				} catch (ServiceException | InterruptedException e) {
					throw new IllegalStateException(e);
				}
			}
		});
		
		entityService.flush();
		entityService.clear();
		
		// Wait for the task to be consumed
		Thread.sleep(10000);
		
		QueuedTaskHolder taskHolder = taskHolderService.getById(taskHolderId.get());
		assertEquals(TaskStatus.COMPLETED, taskHolder.getStatus());
		assertEquals(TaskResult.SUCCESS, taskHolder.getResult());
		assertEquals("success", result.get());
	}
	
	public static class SelfInterruptingTask<T> extends SimpleTestTask<T> {
		private static final long serialVersionUID = 1L;
		
		@Autowired
		private IQueuedTaskHolderManager manager;

		protected SelfInterruptingTask() {
			super();
		}

		public SelfInterruptingTask(StaticValueAccessor<T> valueAccessor, T expectedValue,
				TaskExecutionResult expectedResult) {
			super(valueAccessor, expectedValue, expectedResult);
		}
		
		@Override
		protected TaskExecutionResult doTask() throws Exception {
			manager.stop();
			return super.doTask();
		}
	}
	
	@Test
	public void interrupt() throws Exception {
		final StaticValueAccessor<String> result = new StaticValueAccessor<>();
		final StaticValueAccessor<Long> taskHolderId = new StaticValueAccessor<>();
		transactionTemplate.execute(new TransactionCallbackWithoutResult() {
			@Override
			public void doInTransactionWithoutResult(TransactionStatus status) {
				try {
					// This task will stop the manager during its execution
					SelfInterruptingTask<String> testTask =
							new SelfInterruptingTask<>(result, "success", TaskExecutionResult.completed());
					testTask.setTimeToWaitMs(2000);
					QueuedTaskHolder taskHolder = manager.submit(testTask);
					taskHolderId.set(taskHolder.getId());
				} catch (ServiceException e) {
					throw new IllegalStateException(e);
				}
			}
		});
		
		entityService.flush();
		entityService.clear();
		
		Thread.sleep(10000);
		
		QueuedTaskHolder taskHolder = taskHolderService.getById(taskHolderId.get());
		assertEquals(TaskStatus.INTERRUPTED, taskHolder.getStatus());
		assertNull(taskHolder.getResult());
		assertNull(result.get());
	}

}
