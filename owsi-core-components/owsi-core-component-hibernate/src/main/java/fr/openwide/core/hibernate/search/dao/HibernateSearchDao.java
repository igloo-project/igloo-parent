package fr.openwide.core.hibernate.search.dao;

import fr.openwide.core.hibernate.exception.ServiceException;


public interface HibernateSearchDao {

	void reindexAll() throws ServiceException;
	
}
