package fr.openwide.core.jpa.security.business.authority.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.openwide.core.jpa.business.generic.service.GenericEntityServiceImpl;
import fr.openwide.core.jpa.security.business.authority.dao.AuthorityDao;
import fr.openwide.core.jpa.security.business.authority.model.Authority;
import fr.openwide.core.jpa.security.business.authority.model.Authority_;

@Service("authorityService")
public class AuthorityServiceImpl extends GenericEntityServiceImpl<Integer, Authority>
		implements AuthorityService {

	@Autowired
	public AuthorityServiceImpl(AuthorityDao authorityDao) {
		super(authorityDao);
	}
	
	public Authority getByName(String name) {
		return getByField(Authority_.name, name);
	}

}