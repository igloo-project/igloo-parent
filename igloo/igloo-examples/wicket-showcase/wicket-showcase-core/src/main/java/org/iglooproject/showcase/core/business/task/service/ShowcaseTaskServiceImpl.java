package org.iglooproject.showcase.core.business.task.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.iglooproject.jpa.business.generic.service.GenericEntityServiceImpl;
import org.iglooproject.jpa.exception.ServiceException;
import org.iglooproject.jpa.more.business.task.model.QueuedTaskHolder;
import org.iglooproject.showcase.core.business.task.dao.IShowcaseTaskDao;
import org.iglooproject.showcase.core.business.task.model.search.TaskSearchQueryParameters;

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
