package fr.openwide.jpa.example.config.spring;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

import fr.openwide.core.jpa.config.spring.AbstractJpaConfig;
import fr.openwide.core.jpa.config.spring.DefaultJpaConfig;
import fr.openwide.core.jpa.config.spring.JpaConfigUtils;
import fr.openwide.core.jpa.config.spring.provider.JpaPackageScanProvider;
import fr.openwide.jpa.example.business.CoreJpaExampleBusinessPackage;

@Configuration
@EnableAspectJAutoProxy
@Import(DefaultJpaConfig.class)
public class CoreJpaExampleJpaConfig extends AbstractJpaConfig {

	@Autowired
	private DefaultJpaConfig defaultJpaConfig;

	@Override
	public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
		return JpaConfigUtils.entityManagerFactory(defaultJpaConfig.defaultJpaCoreConfigurationProvider());
	}

	@Override
	public DataSource dataSource() {
		return JpaConfigUtils.dataSource(defaultJpaConfig.defaultTomcatPoolConfigurationProvider());
	}

	@Override
	public JpaPackageScanProvider applicationJpaPackageScanProvider() {
		return new JpaPackageScanProvider(CoreJpaExampleBusinessPackage.class.getPackage());
	}

}
