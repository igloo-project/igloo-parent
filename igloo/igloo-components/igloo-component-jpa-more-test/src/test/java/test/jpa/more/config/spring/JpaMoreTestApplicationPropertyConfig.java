package test.jpa.more.config.spring;

import org.iglooproject.jpa.more.business.parameter.dao.ParameterDaoImpl;
import org.iglooproject.jpa.more.config.spring.JpaMoreTaskApplicationPropertyRegistryConfig;
import org.iglooproject.jpa.more.rendering.service.EmptyRendererServiceImpl;
import org.iglooproject.jpa.more.rendering.service.IRendererService;
import org.iglooproject.jpa.more.util.transaction.CoreJpaMoreUtilTransactionPackage;
import org.iglooproject.spring.autoconfigure.SpringPropertyRegistryAutoConfiguration;
import org.iglooproject.spring.config.spring.IPropertyRegistryConfig;
import org.iglooproject.spring.property.dao.IMutablePropertyDao;
import org.iglooproject.spring.property.service.IPropertyRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import test.jpa.more.business.JpaMoreTestBusinessPackage;

@Configuration
@Import({ JpaMoreTaskApplicationPropertyRegistryConfig.class, SpringPropertyRegistryAutoConfiguration.class })
@ComponentScan(basePackageClasses = { CoreJpaMoreUtilTransactionPackage.class, JpaMoreTestBusinessPackage.class })
public class JpaMoreTestApplicationPropertyConfig implements IPropertyRegistryConfig {

	@Override
	public void register(IPropertyRegistry registry) {
	}

	@Bean
	public IRendererService rendererService() {
		return new EmptyRendererServiceImpl();
	}

	@Bean
	public IMutablePropertyDao mutablePropertyDao() {
		return new ParameterDaoImpl();
	}

}
