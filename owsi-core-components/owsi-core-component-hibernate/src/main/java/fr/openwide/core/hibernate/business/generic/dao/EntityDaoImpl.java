package fr.openwide.core.hibernate.business.generic.dao;

import java.io.Serializable;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;

import fr.openwide.core.hibernate.business.generic.model.GenericEntity;

@Repository("entityDao")
public class EntityDaoImpl extends HibernateDaoSupport implements EntityDao {
	
	@Autowired
	public EntityDaoImpl(SessionFactory sessionFactory) {
		setSessionFactory(sessionFactory);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <K extends Serializable & Comparable<K>, E extends GenericEntity<K, E>> E getEntity(
			Class<E> clazz, K id) {
		return (E) getSession().get(clazz, id);
	}

}
