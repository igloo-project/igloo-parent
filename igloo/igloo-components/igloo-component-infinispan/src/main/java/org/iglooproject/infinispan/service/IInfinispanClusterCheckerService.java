package org.iglooproject.infinispan.service;

import java.util.Collection;
import java.util.concurrent.TimeUnit;

import org.iglooproject.infinispan.model.AddressWrapper;

public interface IInfinispanClusterCheckerService {

	boolean updateCoordinator(AddressWrapper newCoordinator, Collection<AddressWrapper> knownNodes);

	boolean updateCoordinatorTimestamp(AddressWrapper currentCoordinator);

	boolean unsetCoordinator(AddressWrapper oldCoordinator);

	boolean isClusterActive(Collection<AddressWrapper> clusterNodes);

	boolean tryForceUpdate(AddressWrapper currentCoordinator, int delay, TimeUnit unit);

}
