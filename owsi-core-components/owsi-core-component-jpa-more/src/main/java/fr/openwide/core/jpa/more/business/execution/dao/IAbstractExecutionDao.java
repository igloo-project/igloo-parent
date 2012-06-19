package fr.openwide.core.jpa.more.business.execution.dao;

import fr.openwide.core.jpa.business.generic.dao.IGenericEntityDao;
import fr.openwide.core.jpa.more.business.execution.model.AbstractExecution;

public interface IAbstractExecutionDao<E extends AbstractExecution<E, ?>> extends IGenericEntityDao<Integer, E> {

}
