package fr.openwide.core.spring.infinispan.property;

import java.util.Set;

import fr.openwide.core.spring.property.model.AbstractPropertyIds;
import fr.openwide.core.spring.property.model.ImmutablePropertyId;

public final class InfinispanPropertyIds extends AbstractPropertyIds {
	
	private InfinispanPropertyIds() {
	}

	/*
	 * Mutable Properties
	 */
	
	// None
	
	/*
	 * Immutable Properties
	 */
	
	public static final ImmutablePropertyId<Boolean> INFINISPAN_ENABLED = immutable("infinispan.enabled");
	public static final ImmutablePropertyId<String> INFINISPAN_CLUSTER_NAME = immutable("infinispan.clusterName");
	public static final ImmutablePropertyId<String> INFINISPAN_NODE_NAME = immutable("infinispan.nodeName");
	public static final ImmutablePropertyId<Set<String>> INFINISPAN_ROLES = immutable("infinispan.roles");
}
