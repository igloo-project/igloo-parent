package org.iglooproject.jpa.more.business.task.service;

import java.util.Collection;
import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.iglooproject.jpa.more.business.task.model.AbstractTask;
import org.iglooproject.jpa.more.business.task.model.IQueueId;
import org.iglooproject.jpa.more.business.task.model.QueuedTaskHolder;
import org.springframework.transaction.annotation.Transactional;

public interface IQueuedTaskHolderManager {

  boolean isAvailableForAction();

  boolean isActive();

  boolean isTaskQueueActive(String queueId);

  int getNumberOfTaskConsumer(String queueId);

  int getNumberOfWaitingTasks();

  int getNumberOfWaitingTasks(String queueId);

  int getNumberOfRunningTasks();

  int getNumberOfRunningTasks(String queueId);

  Collection<IQueueId> getQueueIds();

  void start();

  void stop();

  @Transactional
  QueuedTaskHolder submit(AbstractTask task) throws ServiceException;

  @Transactional
  void reload(Long queuedTaskHolderId) throws ServiceException, SecurityServiceException;

  @Transactional
  void cancel(Long queuedTaskHolderId) throws ServiceException, SecurityServiceException;

  void onTaskFinish(Long id);
}
