package fr.openwide.core.hibernate.search.service;

import fr.openwide.core.hibernate.exception.ServiceException;

public interface HibernateSearchService {

	void reindexAll() throws ServiceException;
}
