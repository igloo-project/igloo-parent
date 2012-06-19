package fr.openwide.core.jpa.more.business.execution.dao;

import java.util.Date;
import java.util.List;

import fr.openwide.core.jpa.business.generic.dao.IGenericEntityDao;
import fr.openwide.core.jpa.more.business.execution.model.AbstractExecution;
import fr.openwide.core.jpa.more.business.execution.model.ExecutionStatus;
import fr.openwide.core.jpa.more.business.execution.model.IExecutionType;

public interface IAbstractExecutionDao<E extends AbstractExecution<E, ?>> extends IGenericEntityDao<Long, E> {

	List<E> listOrderedByDateTypeStatus(Date startDate, Date endDate, IExecutionType executionType,
			ExecutionStatus executionStatus, Integer limit, Integer offset);

}
