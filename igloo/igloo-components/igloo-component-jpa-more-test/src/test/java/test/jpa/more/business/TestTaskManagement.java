package test.jpa.more.business;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.common.collect.Maps;
import com.google.common.util.concurrent.RateLimiter;
import java.io.Serializable;
import java.text.MessageFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import org.iglooproject.functional.Supplier2;
import org.iglooproject.jpa.business.generic.service.IEntityService;
import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.iglooproject.jpa.more.business.task.model.AbstractTask;
import org.iglooproject.jpa.more.business.task.model.QueuedTaskHolder;
import org.iglooproject.jpa.more.business.task.model.TaskExecutionResult;
import org.iglooproject.jpa.more.business.task.service.IQueuedTaskHolderManager;
import org.iglooproject.jpa.more.business.task.service.IQueuedTaskHolderService;
import org.iglooproject.jpa.more.business.task.util.TaskResult;
import org.iglooproject.jpa.more.business.task.util.TaskStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;
import test.jpa.more.business.task.config.TestTaskManagementConfig;
import test.jpa.more.business.task.model.TestQueueId;
import test.jpa.more.config.spring.SpringBootTestJpaMore;

@SpringBootTestJpaMore
@ContextConfiguration(classes = TestTaskManagementConfig.class)
class TestTaskManagement extends AbstractJpaMoreTestCase {

  @Autowired private IEntityService entityService;

  @Autowired private IQueuedTaskHolderManager manager;

  @Autowired private IQueuedTaskHolderService taskHolderService;

  private TransactionTemplate transactionTemplate;

  /**
   * A utility used to check that a given task has been correctly executed.
   *
   * <p>Designed to always reference the same value, even having been serialized with Jackson.
   */
  protected static class StaticValueAccessor<T> implements Supplier2<T>, Serializable {
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

  private abstract static class AbstractTestTask extends AbstractTask {
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

    protected SimpleTestTask() {}

    public SimpleTestTask(
        StaticValueAccessor<T> valueAccessor, T expectedValue, TaskExecutionResult expectedResult) {
      super();
      this.valueAccessor = valueAccessor;
      this.expectedValue = expectedValue;
      this.expectedResult = expectedResult;
    }

    @Override
    protected TaskExecutionResult doTask() throws Exception {
      if (timeToWaitMs != 0) {
        Thread.sleep(timeToWaitMs);
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

    public int getTimeToWaitMs() {
      return timeToWaitMs;
    }

    public void setTimeToWaitMs(int timeToWaitMs) {
      this.timeToWaitMs = timeToWaitMs;
    }
  }

  @Autowired
  public void setTransactionManager(PlatformTransactionManager transactionManager) {
    transactionTemplate = new TransactionTemplate(transactionManager);
  }

  @Override
  protected void cleanAll() throws ServiceException, SecurityServiceException {
    cleanEntities(taskHolderService);
    super.cleanAll();
  }

  protected void waitTaskConsumption() {
    waitTaskConsumption(false, true);
  }

  protected void waitTaskConsumption(boolean returnIfStopped, boolean waitForRunning) {
    RateLimiter rateLimiter = RateLimiter.create(1);
    int tryCount = 0;
    while (true) {
      tryCount++;
      rateLimiter.acquire();

      if (
      // if returnIfStopped == true, we consider that wait is done
      (returnIfStopped || manager.isActive())
          && manager.getNumberOfWaitingTasks() == 0
          // if we don't wait for running, ignore manager.getNumberOfRunningTasks()
          && (!waitForRunning || manager.getNumberOfRunningTasks() == 0)) {
        break;
      }

      if (tryCount > 10) {
        throw new IllegalStateException(
            MessageFormat.format("Task queue not empty after {0} tries.", tryCount));
      }
    }
  }

  @Test
  void testInitQueue() {
    assertThat(manager.getQueueIds())
        .containsExactlyInAnyOrderElementsOf(List.of(TestQueueId.values()));
  }

  @Test
  void simple() throws Exception {
    final StaticValueAccessor<String> result = new StaticValueAccessor<>();
    final StaticValueAccessor<Long> taskHolderId = new StaticValueAccessor<>();
    transactionTemplate.execute(
        new TransactionCallbackWithoutResult() {
          @Override
          public void doInTransactionWithoutResult(TransactionStatus status) {
            try {
              QueuedTaskHolder taskHolder =
                  manager.submit(
                      new SimpleTestTask<>(result, "success", TaskExecutionResult.completed()));
              taskHolderId.set(taskHolder.getId());
            } catch (ServiceException e) {
              throw new IllegalStateException(e);
            }
          }
        });

    entityService.flush();
    entityService.clear();

    waitTaskConsumption();

    QueuedTaskHolder taskHolder = taskHolderService.getById(taskHolderId.get());
    assertEquals(TaskStatus.COMPLETED, taskHolder.getStatus());
    assertEquals(TaskResult.SUCCESS, taskHolder.getResult());
    assertEquals("success", result.get());
  }

  @Test
  void noTransaction() throws Exception {
    final StaticValueAccessor<String> result = new StaticValueAccessor<>();
    QueuedTaskHolder taskHolder =
        manager.submit(new SimpleTestTask<>(result, "success", TaskExecutionResult.completed()));

    entityService.flush();
    entityService.clear();

    waitTaskConsumption();

    taskHolder = taskHolderService.getById(taskHolder.getId());
    assertEquals(TaskStatus.COMPLETED, taskHolder.getStatus());
    assertEquals(TaskResult.SUCCESS, taskHolder.getResult());
    assertEquals("success", result.get());
  }

  /**
   * Queue submit is transaction-aware ; we test here that task is pushed to queue after transaction
   * commit.
   */
  @Test
  void submitInLongTransaction() throws Exception {
    final StaticValueAccessor<String> result = new StaticValueAccessor<>();
    final StaticValueAccessor<String> result2 = new StaticValueAccessor<>();
    final StaticValueAccessor<Long> taskHolderId = new StaticValueAccessor<>();
    // concurrency mechanism to ensure that task 1 is pushed to queue before task 2
    // for each step, offer then take must be done
    final LinkedBlockingQueue<Boolean> step1ConcurrentLinkedQueue =
        new LinkedBlockingQueue<Boolean>(1);
    final LinkedBlockingQueue<Boolean> step2ConcurrentLinkedQueue =
        new LinkedBlockingQueue<Boolean>(1);

    Runnable runnable =
        new Runnable() {
          @Override
          public void run() {
            transactionTemplate.execute(
                new TransactionCallbackWithoutResult() {
                  @Override
                  public void doInTransactionWithoutResult(TransactionStatus status) {
                    try {
                      QueuedTaskHolder taskHolder =
                          manager.submit(
                              new SimpleTestTask<>(
                                  result, "success", TaskExecutionResult.completed()));
                      // signal that submit is called for task 1
                      step1ConcurrentLinkedQueue.offer(true); // STEP 1 signal

                      taskHolderId.set(taskHolder.getId());

                      // wait for task 2 to be pushed and committed
                      step2ConcurrentLinkedQueue.take(); // STEP 2 wait

                      // Check that the task has not been consumed during this transaction
                      // (which could be aborted)
                      // and that task 2 is allowed to be done
                      assertNull(result.get());
                    } catch (ServiceException e) {
                      throw new IllegalStateException(e);
                    } catch (InterruptedException e) {
                      throw new IllegalStateException(e);
                    }
                  }
                });
          }
        };
    // thread needed so that second task can be run and completed before the above transaction
    Thread t = new Thread(runnable);
    t.start();

    // wait task 1 submit (submitted but not committed, so task not in queue)
    step1ConcurrentLinkedQueue.take(); // STEP 1 wait

    // push another task
    transactionTemplate.execute(
        new TransactionCallbackWithoutResult() {
          @Override
          public void doInTransactionWithoutResult(TransactionStatus status) {
            try {
              manager.submit(
                  new SimpleTestTask<>(result2, "success", TaskExecutionResult.completed()));
            } catch (ServiceException e) {
              throw new IllegalStateException(e);
            }
          }
        });

    // signal that task 2 submit transaction is completed
    step2ConcurrentLinkedQueue.offer(true); // STEP 2 signal

    // wait for task 2 transaction & post-transaction completion
    while (t.isAlive()) {
      t.join();
    }

    waitTaskConsumption();

    entityManagerClear();

    // finally, task 2 is done
    QueuedTaskHolder taskHolder = taskHolderService.getById(taskHolderId.get());
    assertEquals(TaskStatus.COMPLETED, taskHolder.getStatus());
    assertEquals(TaskResult.SUCCESS, taskHolder.getResult());
    assertEquals("success", result.get());
  }

  public static class SelfInterruptingTask<T> extends SimpleTestTask<T> {
    private static final long serialVersionUID = 1L;

    @Autowired private IQueuedTaskHolderManager manager;

    protected SelfInterruptingTask() {
      super();
    }

    public SelfInterruptingTask(
        StaticValueAccessor<T> valueAccessor, T expectedValue, TaskExecutionResult expectedResult) {
      super(valueAccessor, expectedValue, expectedResult);
    }

    @Override
    protected TaskExecutionResult doTask() throws Exception {
      Thread thread =
          new Thread(
              new Runnable() {
                @Override
                public void run() {
                  manager.stop();
                }
              });
      // stop the manager in a separate thread so that there is no lockup
      // (as manager will wait uninterruptibly for all threads to stop)
      thread.start();
      // wait forever (we are going to be interrupted by manager stop)
      TimeUnit.HOURS.sleep(1);
      return super.doTask();
    }
  }

  @Test
  void interrupt() throws Exception {
    final StaticValueAccessor<String> result = new StaticValueAccessor<>();
    final StaticValueAccessor<Long> taskHolderId = new StaticValueAccessor<>();
    transactionTemplate.execute(
        new TransactionCallbackWithoutResult() {
          @Override
          public void doInTransactionWithoutResult(TransactionStatus status) {
            try {
              // This task will stop the manager during its execution
              SelfInterruptingTask<String> testTask =
                  new SelfInterruptingTask<>(result, "success", TaskExecutionResult.completed());
              // we want to force an interruption
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

    waitTaskConsumption(true, true);

    QueuedTaskHolder taskHolder = taskHolderService.getById(taskHolderId.get());
    assertEquals(TaskStatus.INTERRUPTED, taskHolder.getStatus());
    assertEquals(TaskResult.FATAL, taskHolder.getResult());
    assertNull(result.get());
  }
}
