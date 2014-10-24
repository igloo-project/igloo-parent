package fr.openwide.core.showcase.core.business.task.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.openwide.core.jpa.business.generic.service.GenericEntityServiceImpl;
import fr.openwide.core.jpa.exception.ServiceException;
import fr.openwide.core.jpa.more.business.task.model.QueuedTaskHolder;
import fr.openwide.core.showcase.core.business.task.dao.IShowcaseTaskDao;
import fr.openwide.core.showcase.core.business.task.model.search.TaskSearchQueryParameters;

@Service("showcaseTaskService")
public class ShowcaseTaskServiceImpl extends GenericEntityServiceImpl<Long, QueuedTaskHolder> implements
		IShowcaseTaskService {

	private IShowcaseTaskDao showcaseTaskDao;

	@Autowired
	public ShowcaseTaskServiceImpl(IShowcaseTaskDao showcaseTaskDao) {
		super(showcaseTaskDao);
		this.showcaseTaskDao = showcaseTaskDao;
	}

	@Override
	public List<QueuedTaskHolder> search(TaskSearchQueryParameters searchParameters, Long limit, Long offset)
			throws ServiceException {
		return showcaseTaskDao.search(searchParameters, limit, offset);
	}

	@Override
	public long count(TaskSearchQueryParameters searchParameters) throws ServiceException {
		return showcaseTaskDao.count(searchParameters);
	}
}
