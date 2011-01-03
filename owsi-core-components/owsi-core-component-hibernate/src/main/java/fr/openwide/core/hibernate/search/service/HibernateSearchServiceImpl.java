package fr.openwide.core.hibernate.search.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.openwide.core.hibernate.exception.ServiceException;
import fr.openwide.core.hibernate.search.dao.HibernateSearchDao;

@Service("hibernateSearchService")
public class HibernateSearchServiceImpl implements HibernateSearchService {
	
	@Autowired
	private HibernateSearchDao hibernateSearchDao;

	@Override
	public void reindexAll() throws ServiceException {
		hibernateSearchDao.reindexAll();
	}

}
