package fr.openwide.core.jpa.more.business.execution.service;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;

import fr.openwide.core.jpa.business.generic.service.GenericEntityServiceImpl;
import fr.openwide.core.jpa.exception.SecurityServiceException;
import fr.openwide.core.jpa.exception.ServiceException;
import fr.openwide.core.jpa.more.business.execution.dao.IAbstractExecutionDao;
import fr.openwide.core.jpa.more.business.execution.model.AbstractExecution;
import fr.openwide.core.jpa.more.business.execution.model.ExecutionStatus;
import fr.openwide.core.jpa.more.business.execution.model.IExecutionType;

public abstract class AbstractExecutionServiceImpl<E extends AbstractExecution<E, ET>, ET extends IExecutionType>
	extends GenericEntityServiceImpl<Long, E> implements IAbstractExecutionService<E, ET> {

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private IAbstractExecutionDao<E> abstractExecutionDao;

	@Autowired
	public AbstractExecutionServiceImpl(IAbstractExecutionDao<E> abstractExecutionDao) {
		super(abstractExecutionDao);
		this.abstractExecutionDao = abstractExecutionDao;
	}

	@Override
	public E start(E execution, ET type) throws ServiceException, SecurityServiceException {
		execution.setExecutionStatus(ExecutionStatus.RUNNING);
		execution.setExecutionType(type);
		execution.setStartDate(new Date());
		create(execution);
		return execution;
	}

	@Override
	public void close(E execution, ExecutionStatus executionStatus) throws ServiceException, SecurityServiceException {
		execution = entityManager.merge(execution);
		execution.setEndDate(new Date());
		execution.setExecutionStatus(executionStatus);
		update(execution);
	}

	@Override
	public List<E> listOrdered(Integer limit, Integer offset) {
		return abstractExecutionDao.listOrderedByTypeStatus(null, null, limit, offset);
	}

	@Override
	public List<E> listOrderedByDateTypeStatus(Date dateStart, Date dateEnd, IExecutionType executionType,
			ExecutionStatus executionStatus, Integer limit, Integer offset) {
		return abstractExecutionDao.listOrderedByTypeStatus(executionType, executionStatus, limit, offset);
	}
}
