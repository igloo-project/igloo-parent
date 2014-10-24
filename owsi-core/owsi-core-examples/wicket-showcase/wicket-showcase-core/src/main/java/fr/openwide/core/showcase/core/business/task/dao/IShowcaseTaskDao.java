package fr.openwide.core.showcase.core.business.task.dao;

import java.util.List;

import fr.openwide.core.jpa.business.generic.dao.IGenericEntityDao;
import fr.openwide.core.jpa.exception.ServiceException;
import fr.openwide.core.jpa.more.business.task.model.QueuedTaskHolder;
import fr.openwide.core.showcase.core.business.task.model.search.TaskSearchQueryParameters;

public interface IShowcaseTaskDao extends IGenericEntityDao<Long, QueuedTaskHolder> {

	List<QueuedTaskHolder> search(TaskSearchQueryParameters searchParameters, Long limit, Long offset)
			throws ServiceException;

	long count(TaskSearchQueryParameters searchParameters) throws ServiceException;

}
