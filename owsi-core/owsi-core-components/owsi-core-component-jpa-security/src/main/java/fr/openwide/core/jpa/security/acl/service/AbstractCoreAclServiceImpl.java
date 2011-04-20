package fr.openwide.core.jpa.security.acl.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.security.acls.domain.AccessControlEntryImpl;
import org.springframework.security.acls.domain.BasePermission;
import org.springframework.security.acls.domain.GrantedAuthoritySid;
import org.springframework.security.acls.domain.PrincipalSid;
import org.springframework.security.acls.model.AccessControlEntry;
import org.springframework.security.acls.model.Acl;
import org.springframework.security.acls.model.AclService;
import org.springframework.security.acls.model.NotFoundException;
import org.springframework.security.acls.model.ObjectIdentity;
import org.springframework.security.acls.model.Permission;
import org.springframework.security.acls.model.Sid;

import fr.openwide.core.jpa.business.generic.model.GenericEntity;
import fr.openwide.core.jpa.security.acl.domain.CoreAcl;
import fr.openwide.core.jpa.security.acl.domain.PersonGroupSid;
import fr.openwide.core.jpa.security.acl.domain.hierarchy.IPermissionHierarchy;
import fr.openwide.core.jpa.security.business.authority.util.CoreAuthorityConstants;
import fr.openwide.core.jpa.security.business.person.model.IPerson;
import fr.openwide.core.jpa.security.business.person.model.IPersonGroup;

public abstract class AbstractCoreAclServiceImpl extends HibernateDaoSupport implements AclService {
	
	@Autowired
	private IPermissionHierarchy permissionHierarchy;
	
	@Autowired
	private IPermissionRegistryService permissionRegistryService;
	
	@Autowired
	public AbstractCoreAclServiceImpl(SessionFactory sessionFactory) {
		setSessionFactory(sessionFactory);
	}
	
	@Override
	public List<ObjectIdentity> findChildren(ObjectIdentity parentIdentity) {
		return new ArrayList<ObjectIdentity>();
	}

	@Override
	public Acl readAclById(ObjectIdentity objectIdentity) throws NotFoundException {
		return readAclById(objectIdentity, null);
	}

	@Override
	public Acl readAclById(ObjectIdentity objectIdentity, List<Sid> requiredSids) throws NotFoundException {
		return getAcl(objectIdentity, requiredSids);
	}

	@Override
	public Map<ObjectIdentity, Acl> readAclsById(List<ObjectIdentity> objectIdentities) throws NotFoundException {
		return readAclsById(objectIdentities, null);
	}

	@Override
	public Map<ObjectIdentity, Acl> readAclsById(List<ObjectIdentity> objectIdentities, List<Sid> requiredSids) throws NotFoundException {
		Map<ObjectIdentity, Acl> map = new HashMap<ObjectIdentity, Acl>();
		
		for (ObjectIdentity objectIdentity : objectIdentities) {
			map.put(objectIdentity, getAcl(objectIdentity, requiredSids));
		}
		
		return map;
	}
	
	@SuppressWarnings("unchecked")
	private GenericEntity<Integer, ?> getGenericEntity(ObjectIdentity objectIdentity) throws NotFoundException {
		try {
			Class<?> clazz = Class.forName(objectIdentity.getType());
			
			if (GenericEntity.class.isAssignableFrom(clazz) && objectIdentity.getIdentifier() instanceof Integer) {
				return (GenericEntity<Integer, ?>) getSession().get(clazz, objectIdentity.getIdentifier());
			}
		} catch (ClassNotFoundException e) {
			throw new NotFoundException("objectIdentity " + objectIdentity + " is not a valid GenericEntity", e);
		}
		
		throw new NotFoundException("objectIdentity " + objectIdentity + " is not a valid GenericEntity");
	}
	
	private Acl getAcl(ObjectIdentity objectIdentity, List<Sid> requiredSids) throws NotFoundException {
		GenericEntity<Integer, ?> objectIdentityEntity = getGenericEntity(objectIdentity);
		
		CoreAcl acl = new CoreAcl(permissionHierarchy, objectIdentity, requiredSids);
		
		List<AccessControlEntry> aces = getAccessControlEntriesForEntity(acl, objectIdentityEntity);
		if (aces != null) {
			aces.addAll(getAdminAccessControlEntries(acl));
			
			addRequiredAccessControlEntriesToAcl(acl, aces, requiredSids);
			
			return acl;
		}
		
		throw new NotFoundException("objectIdentity " + objectIdentity + " does not support ACL");
	}
	
	protected abstract List<AccessControlEntry> getAccessControlEntriesForEntity(CoreAcl acl, GenericEntity<Integer, ?> objectIdentityEntity)
			throws NotFoundException;
	
	private void addRequiredAccessControlEntriesToAcl(CoreAcl acl, List<AccessControlEntry> aces, List<Sid> requiredSids) {
		for (AccessControlEntry ace : aces) {
			if (requiredSids == null || requiredSids.contains(ace.getSid())) {
				acl.addEntry(ace);
			}
		}
	}
	
	protected List<AccessControlEntry> getAdminAccessControlEntries(CoreAcl acl) {
		List<AccessControlEntry> aces = new ArrayList<AccessControlEntry>();
		
		aces.add(getAccessControlEntry(acl, new GrantedAuthoritySid(CoreAuthorityConstants.ROLE_SYSTEM), BasePermission.ADMINISTRATION));
		aces.add(getAccessControlEntry(acl, new GrantedAuthoritySid(CoreAuthorityConstants.ROLE_ADMIN), BasePermission.ADMINISTRATION));
		
		return aces;
	}
	
	protected AccessControlEntry getAccessControlEntry(CoreAcl acl, String role, Permission permission) {
		return getAccessControlEntry(acl, new GrantedAuthoritySid(role), permission);
	}
	
	protected AccessControlEntry getAccessControlEntry(CoreAcl acl, IPerson person, Permission permission) {
		return getAccessControlEntry(acl, new PrincipalSid(person.getUserName()), permission);
	}
	
	protected AccessControlEntry getAccessControlEntry(CoreAcl acl, IPersonGroup personGroup, Permission permission) {
		return getAccessControlEntry(acl, new PersonGroupSid(personGroup), permission);
	}
	
	protected AccessControlEntry getAccessControlEntry(CoreAcl acl, Sid sid, Permission permission) {
		return new AccessControlEntryImpl(getEntryId(sid, permission), acl, sid, permission, true, false, false);
	}
	
	protected String getEntryId(Sid sid, Permission permission) {
		StringBuilder builder = new StringBuilder();
		
		builder.append(sid.toString());
		builder.append("-");
		builder.append(permissionRegistryService.getPermissionName(permission));
		
		return builder.toString();
	}

}
