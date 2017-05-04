package fr.openwide.core.infinispan.action;

import org.jgroups.Address;

import fr.openwide.core.infinispan.model.impl.SimpleAction;

public class RebalanceAction extends SimpleAction<Boolean> {

	private static final long serialVersionUID = -276444744888760127L;

	public RebalanceAction(Address target) {
		super(target, true, false);
	}

	@Override
	public Boolean call() throws Exception {
		infinispanClusterService.doRebalanceRoles();
		return true;
	}

	public static final RebalanceAction rebalance(Address target) {
		return new RebalanceAction(target);
	}

}
