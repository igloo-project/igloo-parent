package org.iglooproject.jpa.more.business.execution.dao;

import java.util.Date;
import java.util.List;

import org.iglooproject.jpa.business.generic.dao.IGenericEntityDao;
import org.iglooproject.jpa.more.business.execution.model.AbstractExecution;
import org.iglooproject.jpa.more.business.execution.model.ExecutionStatus;
import org.iglooproject.jpa.more.business.execution.model.IExecutionType;

public interface IAbstractExecutionDao<E extends AbstractExecution<E, ?>> extends IGenericEntityDao<Long, E> {

	List<E> listOrderedByDateTypeStatus(Date startDate, Date endDate, IExecutionType executionType,
			ExecutionStatus executionStatus, Integer limit, Integer offset);

}
