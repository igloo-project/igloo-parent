package fr.openwide.core.jpa.more.business.execution.dao;

import java.util.Date;
import java.util.List;

import com.mysema.query.BooleanBuilder;
import com.mysema.query.jpa.impl.JPAQuery;
import com.mysema.query.types.Path;
import com.mysema.query.types.path.EntityPathBase;

import fr.openwide.core.jpa.business.generic.dao.GenericEntityDaoImpl;
import fr.openwide.core.jpa.more.business.execution.model.AbstractExecution;
import fr.openwide.core.jpa.more.business.execution.model.ExecutionStatus;
import fr.openwide.core.jpa.more.business.execution.model.IExecutionType;
import fr.openwide.core.jpa.more.business.execution.model.QAbstractExecution;

public abstract class AbstractExecutionDaoImpl<E extends AbstractExecution<E, ?>>
		extends GenericEntityDaoImpl<Long, E> implements IAbstractExecutionDao<E> {

	@Override
	public List<E> list() {
		Path<E> path = new EntityPathBase<E>(getObjectClass(), QAbstractExecution.abstractExecution.getMetadata());
		QAbstractExecution qAbstractExecution = new QAbstractExecution(path);
		
		return new JPAQuery(getEntityManager())
			.from(qAbstractExecution)
			.orderBy(qAbstractExecution.startDate.desc())
			.list(path);
	}

	@Override
	public List<E> listOrderedByDateTypeStatus(Date startDate, Date endDate, IExecutionType executionType, ExecutionStatus executionStatus,
			Integer limit, Integer offset) {
		Path<E> path = new EntityPathBase<E>(getObjectClass(), QAbstractExecution.abstractExecution.getMetadata());
		QAbstractExecution qAbstractExecution = new QAbstractExecution(path);
		
		BooleanBuilder builder = new BooleanBuilder();
		if (startDate != null) {
			builder.and(qAbstractExecution.startDate.gt(startDate));
		}
		if (endDate != null) {
			builder.and(qAbstractExecution.endDate.lt(endDate));
		}
		if (executionType != null) {
			builder.and(qAbstractExecution.executionType.eq(executionType));
		}
		if (executionStatus != null) {
			builder.and(qAbstractExecution.executionStatus.eq(executionStatus));
		}
		
		JPAQuery jpaQuery = new JPAQuery(getEntityManager())
			.from(qAbstractExecution)
			.orderBy(qAbstractExecution.startDate.desc());
		if (limit != null) {
			jpaQuery.limit(limit);
		}
		if (offset != null) {
			jpaQuery.offset(offset);
		}
		return jpaQuery.list(path);
	}
}
