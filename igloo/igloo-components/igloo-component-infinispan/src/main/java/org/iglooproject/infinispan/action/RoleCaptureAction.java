package org.iglooproject.infinispan.action;

import org.iglooproject.infinispan.model.AddressWrapper;
import org.iglooproject.infinispan.model.IRole;
import org.iglooproject.infinispan.model.impl.SimpleAction;
import org.javatuples.Pair;
import org.jgroups.Address;

public class RoleCaptureAction extends SimpleAction<Pair<SwitchRoleResult, String>> {

	private static final long serialVersionUID = -276444744888760127L;

	private final IRole role;

	public RoleCaptureAction(AddressWrapper target, IRole role) {
		super(target, false, true);
		this.role = role;
	}

	public IRole getRole() {
		return role;
	}

	@Override
	public Pair<SwitchRoleResult, String> call() throws Exception {
		return infinispanClusterService.doCaptureRole(role);
	}

	public static final RoleCaptureAction capture(AddressWrapper target, IRole role) {
		return new RoleCaptureAction(target, role);
	}

}
