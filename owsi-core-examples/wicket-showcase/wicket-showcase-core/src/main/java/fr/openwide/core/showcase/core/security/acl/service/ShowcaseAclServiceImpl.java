package fr.openwide.core.showcase.core.security.acl.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.acls.model.AccessControlEntry;
import org.springframework.security.acls.model.NotFoundException;

import fr.openwide.core.jpa.business.generic.model.GenericEntity;
import fr.openwide.core.jpa.security.acl.domain.CoreAcl;
import fr.openwide.core.jpa.security.acl.service.AbstractCoreAclServiceImpl;

public class ShowcaseAclServiceImpl extends AbstractCoreAclServiceImpl {

	public ShowcaseAclServiceImpl() {
		super();
	}

	@Override
	protected boolean isCacheEnabled() {
		return false;
	}

	@Override
	protected List<AccessControlEntry> getAccessControlEntriesForEntity(
			CoreAcl acl, GenericEntity<Integer, ?> objectIdentityEntity)
			throws NotFoundException {
		return new ArrayList<AccessControlEntry>();
	}
}
