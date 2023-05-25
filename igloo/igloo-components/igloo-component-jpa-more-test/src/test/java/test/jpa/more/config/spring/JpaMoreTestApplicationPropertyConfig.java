package test.jpa.more.config.spring;

import org.iglooproject.jpa.more.business.parameter.dao.ParameterDaoImpl;
import org.iglooproject.jpa.more.config.spring.JpaMoreTaskApplicationPropertyRegistryConfig;
import org.iglooproject.jpa.more.rendering.service.EmptyRendererServiceImpl;
import org.iglooproject.jpa.more.rendering.service.IRendererService;
import org.iglooproject.jpa.more.util.transaction.CoreJpaMoreUtilTransactionPackage;
import org.iglooproject.spring.config.spring.AbstractApplicationPropertyConfig;
import org.iglooproject.spring.config.spring.SpringApplicationPropertyRegistryConfig;
import org.iglooproject.spring.property.dao.IMutablePropertyDao;
import org.iglooproject.spring.property.service.IPropertyRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import test.jpa.more.business.JpaMoreTestBusinessPackage;

@Configuration
@Import({ JpaMoreTaskApplicationPropertyRegistryConfig.class, SpringApplicationPropertyRegistryConfig.class })
@ComponentScan(basePackageClasses = { CoreJpaMoreUtilTransactionPackage.class, JpaMoreTestBusinessPackage.class })
public class JpaMoreTestApplicationPropertyConfig extends AbstractApplicationPropertyConfig {

	@Override
	public void register(IPropertyRegistry registry) {
	}

	@Override
	public IMutablePropertyDao mutablePropertyDao() {
		return new ParameterDaoImpl();
	}

	@Bean
	public IRendererService rendererService() {
		return new EmptyRendererServiceImpl();
	}

}
