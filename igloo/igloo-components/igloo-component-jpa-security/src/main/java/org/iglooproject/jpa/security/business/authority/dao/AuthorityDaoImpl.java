package org.iglooproject.jpa.security.business.authority.dao;

import org.iglooproject.jpa.business.generic.dao.GenericEntityDaoImpl;
import org.iglooproject.jpa.security.business.authority.model.Authority;
import org.iglooproject.jpa.security.business.authority.model.QAuthority;
import org.springframework.stereotype.Repository;

@Repository
public class AuthorityDaoImpl extends GenericEntityDaoImpl<Long, Authority>
    implements IAuthorityDao {

  public AuthorityDaoImpl() {
    super();
  }

  @Override
  public Authority getByName(String name) {
    return super.getByField(QAuthority.authority, QAuthority.authority.name, name);
  }
}
