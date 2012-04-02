package fr.openwide.core.jpa.more.business.task.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.openwide.core.jpa.business.generic.service.GenericEntityServiceImpl;
import fr.openwide.core.jpa.exception.SecurityServiceException;
import fr.openwide.core.jpa.exception.ServiceException;
import fr.openwide.core.jpa.more.business.task.dao.IQueuedTaskHolderDao;
import fr.openwide.core.jpa.more.business.task.model.QueuedTaskHolder;

@Service("queuedTaskHolderService")
public class QueuedTaskHolderServiceImpl extends GenericEntityServiceImpl<Long, QueuedTaskHolder>
		implements IQueuedTaskHolderService {

	private IQueuedTaskHolderDao queuedTaskDao;

	@Autowired
	public QueuedTaskHolderServiceImpl(IQueuedTaskHolderDao queuedTaskDao) {
		super(queuedTaskDao);
		this.queuedTaskDao = queuedTaskDao;
	}

	@Override
	protected void createEntity(QueuedTaskHolder queuedTaskHolder) throws ServiceException, SecurityServiceException {
		queuedTaskHolder.setCreationDate(new Date());
		super.createEntity(queuedTaskHolder);
	}

	@Override
	public QueuedTaskHolder getNextTaskForExecution(String taskType) {
		return queuedTaskDao.getNextTaskForExecution(taskType);
	}

	@Override
	public QueuedTaskHolder getRandomStalledTask(String taskType, int executionTimeLimitInSeconds) {
		return queuedTaskDao.getStalledTask(taskType, executionTimeLimitInSeconds);
	}
}
