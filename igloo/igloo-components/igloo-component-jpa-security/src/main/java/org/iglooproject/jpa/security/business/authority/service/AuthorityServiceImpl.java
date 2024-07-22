package org.iglooproject.jpa.security.business.authority.service;

import org.iglooproject.jpa.business.generic.service.GenericEntityServiceImpl;
import org.iglooproject.jpa.security.business.authority.dao.IAuthorityDao;
import org.iglooproject.jpa.security.business.authority.model.Authority;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthorityServiceImpl extends GenericEntityServiceImpl<Long, Authority>
    implements IAuthorityService {

  private IAuthorityDao dao;

  @Autowired
  public AuthorityServiceImpl(IAuthorityDao dao) {
    super(dao);
    this.dao = dao;
  }

  @Override
  public Authority getByName(String name) {
    return dao.getByName(name);
  }
}
