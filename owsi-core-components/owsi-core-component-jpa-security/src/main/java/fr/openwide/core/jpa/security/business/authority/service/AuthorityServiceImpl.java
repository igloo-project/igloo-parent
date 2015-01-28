package fr.openwide.core.jpa.security.business.authority.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.openwide.core.jpa.business.generic.service.GenericEntityServiceImpl;
import fr.openwide.core.jpa.security.business.authority.dao.IAuthorityDao;
import fr.openwide.core.jpa.security.business.authority.model.Authority;
import fr.openwide.core.jpa.security.business.authority.model.QAuthority;

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
		return authorityDao.getByField(QAuthority.authority, QAuthority.authority.name, name);
	}

}