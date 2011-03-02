package fr.openwide.core.hibernate.security.business.person.dao;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import fr.openwide.core.hibernate.business.generic.dao.GenericEntityDaoImpl;
import fr.openwide.core.hibernate.security.business.person.model.CorePersonGroup;

public class CorePersonGroupDaoImpl extends GenericEntityDaoImpl<Integer, CorePersonGroup> implements PersonGroupDao {

	@Autowired
	public CorePersonGroupDaoImpl(SessionFactory sessionFactory) {
		super(sessionFactory);
	}
	

}