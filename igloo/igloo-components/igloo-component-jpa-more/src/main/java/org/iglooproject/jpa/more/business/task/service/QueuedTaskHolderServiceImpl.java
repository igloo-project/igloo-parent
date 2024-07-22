package org.iglooproject.jpa.more.business.task.service;

import java.time.Instant;
import java.util.List;
import org.iglooproject.jpa.business.generic.service.GenericEntityServiceImpl;
import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.iglooproject.jpa.more.business.task.dao.IQueuedTaskHolderDao;
import org.iglooproject.jpa.more.business.task.model.QueuedTaskHolder;
import org.iglooproject.jpa.more.business.task.util.TaskStatus;
import org.springframework.beans.factory.annotation.Autowired;

public class QueuedTaskHolderServiceImpl extends GenericEntityServiceImpl<Long, QueuedTaskHolder>
    implements IQueuedTaskHolderService {

  private IQueuedTaskHolderDao queuedTaskHolderDao;

  @Autowired
  public QueuedTaskHolderServiceImpl(IQueuedTaskHolderDao queuedTaskHolderDao) {
    super(queuedTaskHolderDao);
    this.queuedTaskHolderDao = queuedTaskHolderDao;
  }

  @Override
  protected void createEntity(QueuedTaskHolder queuedTaskHolder)
      throws ServiceException, SecurityServiceException {
    queuedTaskHolder.setCreationDate(Instant.now());
    super.createEntity(queuedTaskHolder);
  }

  @Override
  public Long count(Instant since, TaskStatus... statuses) {
    return queuedTaskHolderDao.count(since, statuses);
  }

  @Override
  public Long count(TaskStatus... statuses) {
    return queuedTaskHolderDao.count(statuses);
  }

  @Override
  public QueuedTaskHolder getNextTaskForExecution(String taskType) {
    return queuedTaskHolderDao.getNextTaskForExecution(taskType);
  }

  @Override
  public QueuedTaskHolder getRandomStalledTask(String taskType, int executionTimeLimitInSeconds) {
    return queuedTaskHolderDao.getStalledTask(taskType, executionTimeLimitInSeconds);
  }

  @Override
  public List<QueuedTaskHolder> getListConsumable(String queueId)
      throws ServiceException, SecurityServiceException {
    return queuedTaskHolderDao.listConsumable(queueId);
  }

  @Override
  public List<String> listTypes() {
    return queuedTaskHolderDao.listTypes();
  }

  @Override
  public boolean isReloadable(QueuedTaskHolder task) {
    return TaskStatus.RELOADABLE_TASK_STATUS.contains(task.getStatus());
  }

  @Override
  public boolean isCancellable(QueuedTaskHolder task) {
    return TaskStatus.CANCELLABLE_TASK_STATUS.contains(task.getStatus());
  }
}
