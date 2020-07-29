package org.iglooproject.jpa.more.business.execution.dao;

import java.util.Date;
import java.util.List;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.jpa.impl.JPAQuery;

import org.iglooproject.jpa.business.generic.dao.GenericEntityDaoImpl;
import org.iglooproject.jpa.more.business.execution.model.AbstractExecution;
import org.iglooproject.jpa.more.business.execution.model.ExecutionStatus;
import org.iglooproject.jpa.more.business.execution.model.IExecutionType;
import org.iglooproject.jpa.more.business.execution.model.QAbstractExecution;

public abstract class AbstractExecutionDaoImpl<E extends AbstractExecution<E, ?>>
		extends GenericEntityDaoImpl<Long, E> implements IAbstractExecutionDao<E> {

	@Override
	public List<E> list() {
		Path<E> path = new EntityPathBase<>(getObjectClass(), QAbstractExecution.abstractExecution.getMetadata());
		QAbstractExecution qAbstractExecution = new QAbstractExecution(path);
		
		return new JPAQuery<E>(getEntityManager())
			.select(path)
			.from(qAbstractExecution)
			.orderBy(qAbstractExecution.startDate.desc())
			.fetch();
	}

	@Override
	public List<E> listOrderedByDateTypeStatus(Date startDate, Date endDate, IExecutionType executionType, ExecutionStatus executionStatus,
			Integer limit, Integer offset) {
		Path<E> path = new EntityPathBase<>(getObjectClass(), QAbstractExecution.abstractExecution.getMetadata());
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
		
		JPAQuery<E> jpaQuery = new JPAQuery<>(getEntityManager())
			.select(path)
			.from(qAbstractExecution)
			.orderBy(qAbstractExecution.startDate.desc());
		if (limit != null) {
			jpaQuery.limit(limit);
		}
		if (offset != null) {
			jpaQuery.offset(offset);
		}
		return jpaQuery.fetch();
	}
}
