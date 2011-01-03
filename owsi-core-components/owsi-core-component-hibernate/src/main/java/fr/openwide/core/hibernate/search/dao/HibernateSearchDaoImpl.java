package fr.openwide.core.hibernate.search.dao;

import org.hibernate.CacheMode;
import org.hibernate.SessionFactory;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;

import fr.openwide.core.hibernate.exception.ServiceException;

@Repository("hibernateSearchDao")
public class HibernateSearchDaoImpl extends HibernateDaoSupport implements HibernateSearchDao {
	
	@Autowired
	public HibernateSearchDaoImpl(SessionFactory sessionFactory) {
		setSessionFactory(sessionFactory);
	}

	@Override
	public void reindexAll() throws ServiceException {
		try {
			FullTextSession fullTextSession = Search.getFullTextSession(getSession());
			
			fullTextSession.createIndexer()
					.batchSizeToLoadObjects(30)
					.threadsForSubsequentFetching(8)
					.threadsToLoadObjects(4)
					.cacheMode(CacheMode.NORMAL)
					.startAndWait();
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}
	
}