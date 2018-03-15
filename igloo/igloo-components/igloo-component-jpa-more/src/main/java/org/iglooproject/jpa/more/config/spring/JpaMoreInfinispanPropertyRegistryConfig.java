package org.iglooproject.jpa.more.config.spring;

import static org.iglooproject.jpa.more.property.JpaMoreInfinispanPropertyIds.INFINISPAN_CLUSTER_NAME;
import static org.iglooproject.jpa.more.property.JpaMoreInfinispanPropertyIds.INFINISPAN_ENABLED;
import static org.iglooproject.jpa.more.property.JpaMoreInfinispanPropertyIds.INFINISPAN_NODE_NAME;
import static org.iglooproject.jpa.more.property.JpaMoreInfinispanPropertyIds.INFINISPAN_ROLES;
import static org.iglooproject.jpa.more.property.JpaMoreInfinispanPropertyIds.INFINISPAN_ROLES_REBALANCE;
import static org.iglooproject.jpa.more.property.JpaMoreInfinispanPropertyIds.INFINISPAN_TRANSPORT_CONFIGURATION_FILE;
import static org.iglooproject.jpa.more.property.JpaMoreInfinispanPropertyIds.INFINISPAN_TRANSPORT_PROPERTIES;
import static org.iglooproject.jpa.more.property.JpaMoreInfinispanPropertyIds.INFINISPAN_TRANSPORT_PROPERTY;

import java.util.Collections;
import java.util.Set;

import org.iglooproject.functional.Suppliers2;
import org.iglooproject.functional.converter.StringCollectionConverter;
import org.iglooproject.spring.config.spring.AbstractApplicationPropertyRegistryConfig;
import org.iglooproject.spring.property.service.IPropertyRegistry;
import org.springframework.context.annotation.Configuration;

import com.google.common.base.Converter;

@Configuration
public class JpaMoreInfinispanPropertyRegistryConfig extends AbstractApplicationPropertyRegistryConfig {
	
	@Override
	public void register(IPropertyRegistry registry) {
		registry.registerBoolean(INFINISPAN_ENABLED, false);
		registry.registerString(INFINISPAN_NODE_NAME, "node");
		registry.registerString(INFINISPAN_CLUSTER_NAME, "cluster");
		// Set of roles, separated by ','
		registry.register(INFINISPAN_ROLES, new StringCollectionConverter<String, Set<String>>(Converter.identity(), Suppliers2.hashSet()).separator(","), Collections.emptySet());
		// Set of roles, separated by ','
		registry.register(INFINISPAN_ROLES_REBALANCE, new StringCollectionConverter<String, Set<String>>(Converter.identity(), Suppliers2.hashSet()).separator(","), (Set<String>) null);
		// this file is available in infinispan classpath
		registry.registerString(INFINISPAN_TRANSPORT_CONFIGURATION_FILE, "default-configs/default-jgroups-udp.xml");
		// Properties to import for transport configuration, separated by ','
		registry.register(INFINISPAN_TRANSPORT_PROPERTIES, new StringCollectionConverter<String, Set<String>>(Converter.identity(), Suppliers2.hashSet()).separator(","), Collections.emptySet());
		registry.registerString(INFINISPAN_TRANSPORT_PROPERTY);
	}

}
