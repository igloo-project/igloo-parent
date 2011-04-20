package fr.openwide.core.hibernate.security.business.authority.dao;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import fr.openwide.core.hibernate.business.generic.dao.GenericEntityDaoImpl;
import fr.openwide.core.hibernate.security.business.authority.model.Authority;

@Repository("authorityDao")
public class AuthorityDaoImpl extends GenericEntityDaoImpl<Integer, Authority> implements AuthorityDao {
	
	@Autowired
	public AuthorityDaoImpl(SessionFactory sessionFactory) {
		super(sessionFactory);
	}
		
}