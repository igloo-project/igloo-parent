package org.iglooproject.jpa.security.business.authority.service;

import org.iglooproject.jpa.business.generic.service.IGenericEntityService;
import org.iglooproject.jpa.security.business.authority.model.Authority;

public interface IAuthorityService extends IGenericEntityService<Long, Authority> {

  Authority getByName(String name);
}
