package fr.openwide.core.hibernate.security.business.authority.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.openwide.core.hibernate.business.generic.service.GenericEntityServiceImpl;
import fr.openwide.core.hibernate.security.business.authority.dao.AuthorityDao;
import fr.openwide.core.hibernate.security.business.authority.model.Authority;

@Service("authorityService")
public class AuthorityServiceImpl extends GenericEntityServiceImpl<Integer, Authority>
		implements AuthorityService {

	@Autowired
	public AuthorityServiceImpl(AuthorityDao authorityDao) {
		super(authorityDao);
	}
	
	public Authority getByName(String name) {
		return getByField("name", name);
	}

}