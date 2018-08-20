package org.iglooproject.jpa.more.config.spring;

import java.util.Properties;

import javax.persistence.EntityManagerFactory;

import org.iglooproject.infinispan.service.IActionFactory;
import org.iglooproject.infinispan.service.IInfinispanClusterCheckerService;
import org.iglooproject.infinispan.service.IInfinispanClusterService;
import org.iglooproject.infinispan.service.IRolesProvider;
import org.iglooproject.infinispan.service.InfinispanClusterServiceImpl;
import org.iglooproject.infinispan.utils.DefaultReplicatedTransientConfigurationBuilder;
import org.iglooproject.infinispan.utils.GlobalDefaultReplicatedTransientConfigurationBuilder;
import org.iglooproject.infinispan.utils.role.RolesFromStringSetProvider;
import org.iglooproject.jpa.more.config.spring.util.SpringActionFactory;
import org.iglooproject.jpa.more.infinispan.service.IInfinispanQueueTaskManagerService;
import org.iglooproject.jpa.more.infinispan.service.InfinispanClusterJdbcCheckerServiceImpl;
import org.iglooproject.jpa.more.infinispan.service.InfinispanQueueTaskManagerServiceImpl;
import org.iglooproject.jpa.more.property.JpaMoreInfinispanPropertyIds;
import org.iglooproject.spring.property.service.IPropertyService;
import org.infinispan.configuration.global.GlobalConfiguration;
import org.infinispan.manager.DefaultCacheManager;
import org.infinispan.manager.EmbeddedCacheManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({
	JpaMoreInfinispanPropertyRegistryConfig.class
})
public class JpaMoreInfinispanConfig {

	@Bean
	public IRolesProvider rolesProvider(IPropertyService propertyService) {
		// FT - allow bean override
		if (propertyService.get(JpaMoreInfinispanPropertyIds.INFINISPAN_ENABLED)) {
			return new RolesFromStringSetProvider(
					propertyService.get(JpaMoreInfinispanPropertyIds.INFINISPAN_ROLES),
					propertyService.get(JpaMoreInfinispanPropertyIds.INFINISPAN_ROLES_REBALANCE));
		} else {
			return null;
		}
	}

	@Bean
	public IActionFactory actionFactory() {
		return new SpringActionFactory();
	}

	@Bean
	public IInfinispanClusterCheckerService infinispanClusterCheckerService(IPropertyService propertyService) {
		if (propertyService.get(JpaMoreInfinispanPropertyIds.INFINISPAN_ENABLED)) {
			return new InfinispanClusterJdbcCheckerServiceImpl();
		}
		return null;
	}

	@Bean(destroyMethod = "stop")
	public IInfinispanClusterService infinispanClusterService(IPropertyService propertyService,
			@Autowired(required = false) IRolesProvider rolesProvider,
			@Autowired(required = false) IActionFactory springActionFactory,
			@Autowired(required = false) IInfinispanClusterCheckerService infinispanClusterCheckerService,
			EntityManagerFactory entityManagerFactory) {
		if (propertyService.get(JpaMoreInfinispanPropertyIds.INFINISPAN_ENABLED)) {
			String nodeName = propertyService.get(JpaMoreInfinispanPropertyIds.INFINISPAN_NODE_NAME);
			Properties properties = new Properties();
			for (String key : propertyService.get(JpaMoreInfinispanPropertyIds.INFINISPAN_TRANSPORT_PROPERTIES)) {
				properties.put(key, propertyService.getAsString(JpaMoreInfinispanPropertyIds.transportProperty(key)));
			}
			GlobalConfiguration globalConfiguration =
					new GlobalDefaultReplicatedTransientConfigurationBuilder(properties).nodeName(nodeName).build();
			org.infinispan.configuration.cache.Configuration configuration =
					new DefaultReplicatedTransientConfigurationBuilder().build();
			EmbeddedCacheManager cacheManager = new DefaultCacheManager(globalConfiguration, configuration, false);
			
			InfinispanClusterServiceImpl cluster =
					new InfinispanClusterServiceImpl(nodeName, cacheManager, rolesProvider, springActionFactory,
							infinispanClusterCheckerService);
			cluster.init();
			return cluster;
		} else {
			return null;
		}
	}

	@Bean
	public IInfinispanQueueTaskManagerService infinispanQueueTaskManagerService(IPropertyService propertyService) {
		if (propertyService.get(JpaMoreInfinispanPropertyIds.INFINISPAN_ENABLED)) {
			return new InfinispanQueueTaskManagerServiceImpl();
		}
		return null;
	}

}
