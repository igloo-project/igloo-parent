package fr.openwide.core.jpa.more.business.execution.service;

import fr.openwide.core.jpa.business.generic.service.IGenericEntityService;
import fr.openwide.core.jpa.exception.SecurityServiceException;
import fr.openwide.core.jpa.exception.ServiceException;
import fr.openwide.core.jpa.more.business.execution.model.AbstractExecution;
import fr.openwide.core.jpa.more.business.execution.model.IExecutionType;

public interface IAbstractExecutionService<E extends AbstractExecution<E, ET>, ET extends IExecutionType>
		extends IGenericEntityService<Integer, E> {

	E start(E execution, ET type) throws ServiceException, SecurityServiceException;

	void close(E execution, fr.openwide.core.jpa.more.business.execution.model.ExecutionStatus executionStatus)
			throws ServiceException, SecurityServiceException;

}
