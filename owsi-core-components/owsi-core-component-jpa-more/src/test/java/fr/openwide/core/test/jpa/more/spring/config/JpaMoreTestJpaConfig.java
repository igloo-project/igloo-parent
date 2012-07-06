package fr.openwide.core.test.jpa.more.spring.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

import fr.openwide.core.jpa.config.spring.DefaultJpaConfig;
import fr.openwide.core.jpa.config.spring.JpaConfigUtils;
import fr.openwide.core.jpa.config.spring.provider.JpaPackageScanProvider;
import fr.openwide.core.jpa.more.spring.config.AbstractJpaMoreConfig;
import fr.openwide.core.test.jpa.more.business.JpaMoreTestBusinessPackage;

@Configuration
@EnableAspectJAutoProxy
@Import(DefaultJpaConfig.class)
public class JpaMoreTestJpaConfig extends AbstractJpaMoreConfig {

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
		return new JpaPackageScanProvider(JpaMoreTestBusinessPackage.class.getPackage());
	}

}
