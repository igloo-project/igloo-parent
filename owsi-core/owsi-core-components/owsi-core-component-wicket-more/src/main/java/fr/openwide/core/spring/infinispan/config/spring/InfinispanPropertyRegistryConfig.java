package fr.openwide.core.spring.infinispan.config.spring;

import static fr.openwide.core.spring.infinispan.property.InfinispanPropertyIds.INFINISPAN_CLUSTER_NAME;
import static fr.openwide.core.spring.infinispan.property.InfinispanPropertyIds.INFINISPAN_ENABLED;
import static fr.openwide.core.spring.infinispan.property.InfinispanPropertyIds.INFINISPAN_NODE_NAME;
import static fr.openwide.core.spring.infinispan.property.InfinispanPropertyIds.INFINISPAN_ROLES;

import java.util.Set;

import org.springframework.context.annotation.Configuration;

import com.google.common.base.Converter;

import fr.openwide.core.commons.util.functional.Suppliers2;
import fr.openwide.core.commons.util.functional.converter.StringCollectionConverter;
import fr.openwide.core.spring.config.spring.AbstractApplicationPropertyRegistryConfig;
import fr.openwide.core.spring.property.service.IPropertyRegistry;

@Configuration
public class InfinispanPropertyRegistryConfig extends AbstractApplicationPropertyRegistryConfig {
	
	@Override
	protected void register(IPropertyRegistry registry) {
		registry.registerBoolean(INFINISPAN_ENABLED, false);
		registry.registerString(INFINISPAN_NODE_NAME, "node");
		registry.registerString(INFINISPAN_CLUSTER_NAME, "cluster");
		// Set of roles, separated by ','
		registry.register(INFINISPAN_ROLES, new StringCollectionConverter<String, Set<String>>(Converter.identity(), Suppliers2.hashSet()).separator(","));
	}

}
