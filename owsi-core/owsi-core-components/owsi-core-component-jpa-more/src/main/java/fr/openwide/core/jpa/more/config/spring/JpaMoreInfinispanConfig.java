package fr.openwide.core.jpa.more.config.spring;

import java.util.Properties;

import org.infinispan.configuration.global.GlobalConfiguration;
import org.infinispan.manager.DefaultCacheManager;
import org.infinispan.manager.EmbeddedCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import fr.openwide.core.infinispan.service.IActionFactory;
import fr.openwide.core.infinispan.service.IInfinispanClusterService;
import fr.openwide.core.infinispan.service.IRolesProvider;
import fr.openwide.core.infinispan.service.InfinispanClusterServiceImpl;
import fr.openwide.core.infinispan.utils.DefaultReplicatedTransientConfigurationBuilder;
import fr.openwide.core.infinispan.utils.GlobalDefaultReplicatedTransientConfigurationBuilder;
import fr.openwide.core.infinispan.utils.role.RolesFromStringSetProvider;
import fr.openwide.core.jpa.more.config.spring.util.SpringActionFactory;
import fr.openwide.core.jpa.more.property.JpaMoreInfinispanPropertyIds;
import fr.openwide.core.spring.property.service.IPropertyService;

@Configuration
@Import({
	JpaMoreInfinispanPropertyRegistryConfig.class
})
public class JpaMoreInfinispanConfig {

	@Bean
	public IRolesProvider rolesProvider(IPropertyService propertyService) {
		// FT - allow bean override
		if (propertyService.get(JpaMoreInfinispanPropertyIds.INFINISPAN_ENABLED)) {
			return new RolesFromStringSetProvider(propertyService.get(JpaMoreInfinispanPropertyIds.INFINISPAN_ROLES));
		} else {
			return null;
		}
	}

	@Bean
	public IActionFactory actionFactory() {
		return new SpringActionFactory();
	}

	@Bean(destroyMethod="stop")
	public IInfinispanClusterService infinispanCluster(IPropertyService propertyService, IRolesProvider rolesProvider,
			IActionFactory springActionFactory) {
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
					new InfinispanClusterServiceImpl(nodeName, cacheManager, rolesProvider, springActionFactory);
			cluster.init();
			return cluster;
		} else {
			return null;
		}
	}

}
