package fr.openwide.core.jpa.security.acl.domain;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.acls.model.AccessControlEntry;
import org.springframework.security.acls.model.Acl;
import org.springframework.security.acls.model.NotFoundException;
import org.springframework.security.acls.model.ObjectIdentity;
import org.springframework.security.acls.model.Permission;
import org.springframework.security.acls.model.Sid;
import org.springframework.security.acls.model.UnloadedSidException;
import org.springframework.util.Assert;

import fr.openwide.core.jpa.security.acl.domain.hierarchy.PermissionHierarchy;

public class CoreAcl implements Acl {
	
	private static final long serialVersionUID = -4850094246182483507L;
	
	private PermissionHierarchy permissionHierarchy;
	
	private ObjectIdentity objectIdentity;
	
	private List<Sid> loadedSids = new ArrayList<Sid>();
	
	private List<AccessControlEntry> entries = new ArrayList<AccessControlEntry>();
	
	public CoreAcl(PermissionHierarchy permissionHierarchy, ObjectIdentity objectIdentity) {
		this.permissionHierarchy = permissionHierarchy;
		this.objectIdentity = objectIdentity;
	}
	
	public CoreAcl(PermissionHierarchy permissionHierarchy, ObjectIdentity objectIdentity, List<Sid> loadedSids) {
		this.permissionHierarchy = permissionHierarchy;
		this.objectIdentity = objectIdentity;
		this.loadedSids = loadedSids;
	}
	
	@Override
	public ObjectIdentity getObjectIdentity() {
		return objectIdentity;
	}

	@Override
	public List<AccessControlEntry> getEntries() {
		return new ArrayList<AccessControlEntry>(entries);
	}
	
	public void addEntry(AccessControlEntry entry) {
		entries.add(entry);
	}

	@Override
	public Sid getOwner() {
		return null;
	}

	@Override
	public Acl getParentAcl() {
		return null;
	}

	@Override
	public boolean isEntriesInheriting() {
		return false;
	}

	@Override
	public boolean isGranted(List<Permission> permission, List<Sid> sids, boolean administrativeMode)
			throws NotFoundException, UnloadedSidException {
		Assert.notEmpty(permission, "Permissions required");
		Assert.notEmpty(sids, "SIDs required");

		if (!this.isSidLoaded(sids)) {
			throw new UnloadedSidException("ACL was not loaded for one or more SID");
		}
		
		List<Permission> acceptablePermissions = permissionHierarchy.getAcceptablePermissions(permission);

		AccessControlEntry firstRejection = null;

		for (Permission p : acceptablePermissions) {
			for (Sid sid : sids) {
				// Attempt to find exact match for this permission mask and SID
				boolean scanNextSid = true;

				for (AccessControlEntry ace : entries) {

					if ((ace.getPermission().getMask() == p.getMask()) && ace.getSid().equals(sid)) {
						// Found a matching ACE, so its authorization decision will prevail
						if (ace.isGranting()) {
							// Success
							return true;
						}

						// Failure for this permission, so stop search
						// We will see if they have a different permission (this permission is 100% rejected for this SID)
						if (firstRejection == null) {
							// Store first rejection for auditing reasons
							firstRejection = ace;
						}

						scanNextSid = false; // helps break the loop

						break; // exit aces loop
					}
				}

				if (!scanNextSid) {
					break; // exit SID for loop (now try next permission)
				}
			}
		}

		if (firstRejection != null) {
			return false;
		}

		// We either have no parent, or we're the uppermost parent
		throw new NotFoundException("Unable to locate a matching ACE for passed permissions and SIDs");
	}

	@Override
	public boolean isSidLoaded(List<Sid> sids) {
		return loadedSids.containsAll(sids);
	}

}
