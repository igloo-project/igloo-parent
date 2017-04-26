package fr.openwide.core.spring.infinispan.config.spring;

import java.util.Properties;

import org.infinispan.configuration.global.GlobalConfiguration;
import org.infinispan.manager.DefaultCacheManager;
import org.infinispan.manager.EmbeddedCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import fr.openwide.core.infinispan.service.IActionFactory;
import fr.openwide.core.infinispan.service.IInfinispanClusterService;
import fr.openwide.core.infinispan.service.IRolesProvider;
import fr.openwide.core.infinispan.service.InfinispanClusterServiceImpl;
import fr.openwide.core.infinispan.utils.DefaultReplicatedTransientConfigurationBuilder;
import fr.openwide.core.infinispan.utils.GlobalDefaultReplicatedTransientConfigurationBuilder;
import fr.openwide.core.infinispan.utils.role.RolesFromStringSetProvider;
import fr.openwide.core.spring.infinispan.config.spring.util.SpringActionFactory;
import fr.openwide.core.spring.infinispan.property.InfinispanPropertyIds;
import fr.openwide.core.spring.property.service.IPropertyService;
import fr.openwide.core.wicket.more.WicketMorePackage;

@Configuration
@ComponentScan(
		basePackageClasses = WicketMorePackage.class,
		excludeFilters = @Filter(Configuration.class)
)
@Import({
	InfinispanPropertyRegistryConfig.class
})
public class CoreInfinispanConfig {

	@Bean
	public IRolesProvider rolesProvider(IPropertyService propertyService) {
		// FT - allow bean override
		if (propertyService.get(InfinispanPropertyIds.INFINISPAN_ENABLED)) {
			return new RolesFromStringSetProvider(propertyService.get(InfinispanPropertyIds.INFINISPAN_ROLES));
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
		if (propertyService.get(InfinispanPropertyIds.INFINISPAN_ENABLED)) {
			String nodeName = propertyService.get(InfinispanPropertyIds.INFINISPAN_NODE_NAME);
			Properties properties = new Properties();
			for (String key : propertyService.get(InfinispanPropertyIds.INFINISPAN_TRANSPORT_PROPERTIES)) {
				properties.put(key, propertyService.getAsString(InfinispanPropertyIds.transportProperty(key)));
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
