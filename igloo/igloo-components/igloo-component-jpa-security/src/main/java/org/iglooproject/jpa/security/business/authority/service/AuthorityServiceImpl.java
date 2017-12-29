package org.iglooproject.jpa.security.business.authority.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.iglooproject.jpa.business.generic.service.GenericEntityServiceImpl;
import org.iglooproject.jpa.security.business.authority.dao.IAuthorityDao;
import org.iglooproject.jpa.security.business.authority.model.Authority;

@Service("authorityService")
public class AuthorityServiceImpl extends GenericEntityServiceImpl<Long, Authority>
		implements IAuthorityService {

	private IAuthorityDao authorityDao;

	@Autowired
	public AuthorityServiceImpl(IAuthorityDao authorityDao) {
		super(authorityDao);
		this.authorityDao = authorityDao;
	}
	
	@Override
	public Authority getByName(String name) {
		return authorityDao.getByName(name);
	}

}