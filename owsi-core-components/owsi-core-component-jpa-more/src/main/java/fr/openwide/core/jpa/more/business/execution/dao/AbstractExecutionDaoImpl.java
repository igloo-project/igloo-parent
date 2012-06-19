package fr.openwide.core.jpa.more.business.execution.dao;

import java.util.List;

import com.mysema.query.jpa.impl.JPAQuery;
import com.mysema.query.types.Path;
import com.mysema.query.types.path.EntityPathBase;

import fr.openwide.core.jpa.business.generic.dao.GenericEntityDaoImpl;
import fr.openwide.core.jpa.more.business.execution.model.AbstractExecution;
import fr.openwide.core.jpa.more.business.execution.model.QAbstractExecution;

public abstract class AbstractExecutionDaoImpl<E extends AbstractExecution<E, ?>>
		extends GenericEntityDaoImpl<Integer, E> implements IAbstractExecutionDao<E> {

	@Override
	public List<E> list() {
		Path<E> path = new EntityPathBase<E>(getObjectClass(), QAbstractExecution.abstractExecution.getMetadata());
		QAbstractExecution qAbstractExecution = new QAbstractExecution(path);
		
		return new JPAQuery(getEntityManager())
			.from(qAbstractExecution)
			.orderBy(qAbstractExecution.startDate.desc())
			.list(path);
	}
}
