package fr.openwide.core.jpa.more.business.execution.service;

import java.util.Date;
import java.util.List;

import fr.openwide.core.jpa.business.generic.service.IGenericEntityService;
import fr.openwide.core.jpa.exception.SecurityServiceException;
import fr.openwide.core.jpa.exception.ServiceException;
import fr.openwide.core.jpa.more.business.execution.model.AbstractExecution;
import fr.openwide.core.jpa.more.business.execution.model.ExecutionStatus;
import fr.openwide.core.jpa.more.business.execution.model.IExecutionType;

public interface IAbstractExecutionService<E extends AbstractExecution<E, ET>, ET extends IExecutionType>
		extends IGenericEntityService<Long, E> {

	E start(E execution, ET type) throws ServiceException, SecurityServiceException;

	void close(E execution, fr.openwide.core.jpa.more.business.execution.model.ExecutionStatus executionStatus)
			throws ServiceException, SecurityServiceException;

	List<E> listOrdered(Integer limit, Integer offset);

	List<E> listOrderedByDateTypeStatus(Date startDate, Date endDate, IExecutionType executionType,
			ExecutionStatus executionStatus, Integer limit, Integer offset);

}
