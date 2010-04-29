package fr.openwide.hibernate.util.hibernate;

import org.hibernate.FlushMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.orm.hibernate3.SessionHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Component
public class HibernateSessionUtils {
	
	@Autowired
	private SessionFactory sessionFactory;

	public Session initSession() {
		Session session = null;
		if (!TransactionSynchronizationManager.hasResource(sessionFactory)) {
			session = getSession(sessionFactory);
			TransactionSynchronizationManager.bindResource(sessionFactory, new SessionHolder(session));
		}
		return session;
	}
	
	public void closeSession() {
		SessionHolder sessionHolder = 
			(SessionHolder) TransactionSynchronizationManager.unbindResourceIfPossible(sessionFactory);
		if (sessionHolder != null) {
			SessionFactoryUtils.closeSession(sessionHolder.getSession());
		}
	}
	
	private Session getSession(SessionFactory sessionFactory) throws DataAccessResourceFailureException {
		Session session = SessionFactoryUtils.getSession(sessionFactory, true);
		session.setFlushMode(FlushMode.MANUAL);
		return session;
	}
}
