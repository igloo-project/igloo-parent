package fr.openwide.core.jpa.security.business.authority.service;

import fr.openwide.core.jpa.business.generic.service.IGenericEntityService;
import fr.openwide.core.jpa.security.business.authority.model.Authority;

public interface AuthorityService extends IGenericEntityService<Integer, Authority> {

	Authority getByName(String name);
	
}
