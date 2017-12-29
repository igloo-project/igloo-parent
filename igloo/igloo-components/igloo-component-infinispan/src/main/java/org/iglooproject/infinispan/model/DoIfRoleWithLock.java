package org.iglooproject.infinispan.model;

public enum DoIfRoleWithLock {

	RUN,
	NOT_RUN_CLUSTER_UNAVAILABLE,
	NOT_RUN_ROLE_NOT_OWNED,
	NOT_RUN_LOCK_NOT_AVAILABLE;

}
