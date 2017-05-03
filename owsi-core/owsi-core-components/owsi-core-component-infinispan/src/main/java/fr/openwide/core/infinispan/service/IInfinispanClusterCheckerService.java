package fr.openwide.core.infinispan.service;

import java.util.Collection;
import java.util.concurrent.TimeUnit;

import org.jgroups.Address;

public interface IInfinispanClusterCheckerService {

	boolean updateCoordinator(Address newCoordinator, Collection<Address> knownNodes);

	boolean updateCoordinatorTimestamp(Address currentCoordinator);

	boolean unsetCoordinator(Address oldCoordinator);

	boolean isClusterActive(Collection<Address> clusterNodes);

	boolean tryForceUpdate(Address currentCoordinator, int delay, TimeUnit unit);

}
