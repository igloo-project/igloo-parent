package fr.openwide.core.hibernate.security.business.authority.service;

import fr.openwide.core.hibernate.business.generic.service.GenericEntityService;
import fr.openwide.core.hibernate.security.business.authority.model.Authority;

public interface AuthorityService extends GenericEntityService<Integer, Authority> {

	Authority getByName(String name);
	
}
