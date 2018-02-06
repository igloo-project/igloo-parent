package org.iglooproject.infinispan.model;

import java.io.Serializable;
import java.util.concurrent.Future;

import org.iglooproject.infinispan.service.IInfinispanClusterService;

public interface IAction<V> extends Future<V>, Serializable {

	AddressWrapper getTarget();

	boolean isBroadcast();

	boolean needsResult();

	void setInfinispanClusterService(IInfinispanClusterService infinispanClusterService);

	void doRun();

}
