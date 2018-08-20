package org.iglooproject.infinispan.action;

import org.iglooproject.infinispan.model.AddressWrapper;
import org.iglooproject.infinispan.model.impl.SimpleAction;

public class RebalanceAction extends SimpleAction<Boolean> {

	private static final long serialVersionUID = -276444744888760127L;

	public RebalanceAction(AddressWrapper target) {
		super(target, true, false);
	}

	@Override
	public Boolean call() throws Exception {
		infinispanClusterService.doRebalanceRoles();
		return true;
	}

	public static final RebalanceAction rebalance(AddressWrapper target) {
		return new RebalanceAction(target);
	}

}
