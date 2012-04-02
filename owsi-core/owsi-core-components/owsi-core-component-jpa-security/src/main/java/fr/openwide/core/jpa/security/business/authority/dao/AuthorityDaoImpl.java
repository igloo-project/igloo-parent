package fr.openwide.core.jpa.security.business.authority.dao;

import org.springframework.stereotype.Repository;

import fr.openwide.core.jpa.business.generic.dao.GenericEntityDaoImpl;
import fr.openwide.core.jpa.security.business.authority.model.Authority;

@Repository("authorityDao")
public class AuthorityDaoImpl extends GenericEntityDaoImpl<Long, Authority> implements IAuthorityDao {
	
	public AuthorityDaoImpl() {
		super();
	}
		
}