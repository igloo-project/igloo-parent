package org.iglooproject.jpa.more.business.execution.service;

import java.util.Date;
import java.util.List;

import org.iglooproject.jpa.business.generic.service.IGenericEntityService;
import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.iglooproject.jpa.more.business.execution.model.AbstractExecution;
import org.iglooproject.jpa.more.business.execution.model.ExecutionStatus;
import org.iglooproject.jpa.more.business.execution.model.IExecutionType;

public interface IAbstractExecutionService<E extends AbstractExecution<E, ET>, ET extends IExecutionType>
		extends IGenericEntityService<Long, E> {

	E start(E execution, ET type) throws ServiceException, SecurityServiceException;

	void close(E execution, org.iglooproject.jpa.more.business.execution.model.ExecutionStatus executionStatus)
			throws ServiceException, SecurityServiceException;

	List<E> listOrdered(Integer limit, Integer offset);

	List<E> listOrderedByDateTypeStatus(Date startDate, Date endDate, IExecutionType executionType,
			ExecutionStatus executionStatus, Integer limit, Integer offset);

}
