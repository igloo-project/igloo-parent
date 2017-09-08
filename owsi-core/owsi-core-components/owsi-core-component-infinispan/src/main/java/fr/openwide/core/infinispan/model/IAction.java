package fr.openwide.core.infinispan.model;

import java.io.Serializable;
import java.util.concurrent.Future;

import org.jgroups.Address;

import fr.openwide.core.infinispan.service.IInfinispanClusterService;

public interface IAction<V> extends Future<V>, Serializable {

	Address getTarget();

	boolean isBroadcast();

	boolean needsResult();

	void setInfinispanClusterService(IInfinispanClusterService infinispanClusterService);

	void doRun();

}
