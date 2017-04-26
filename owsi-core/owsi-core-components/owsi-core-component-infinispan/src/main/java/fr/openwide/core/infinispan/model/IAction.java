package fr.openwide.core.infinispan.model;

import java.util.concurrent.Future;

import org.infinispan.remoting.transport.Address;

import fr.openwide.core.infinispan.service.IInfinispanClusterService;

public interface IAction<V> extends Future<V> {

	void run();

	Address getTarget();

	void setInfinispanClusterService(IInfinispanClusterService infinispanClusterService);

}
