package fr.openwide.core.infinispan.action;

import org.javatuples.Pair;
import org.jgroups.Address;

import fr.openwide.core.infinispan.model.IRole;
import fr.openwide.core.infinispan.model.impl.SimpleAction;

public class RoleCaptureAction extends SimpleAction<Pair<SwitchRoleResult, String>> {

	private static final long serialVersionUID = -276444744888760127L;

	private final IRole role;

	public RoleCaptureAction(Address target, IRole role) {
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

	public static final RoleCaptureAction capture(Address target, IRole role) {
		return new RoleCaptureAction(target, role);
	}

}
