package fr.openwide.core.showcase.core.config.spring;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

import fr.openwide.core.jpa.config.spring.provider.JpaPackageScanProvider;
import fr.openwide.core.jpa.more.business.JpaMoreBusinessPackage;
import fr.openwide.core.jpa.security.business.JpaSecurityBusinessPackage;
import fr.openwide.core.jpa.security.config.spring.AbstractConfiguredJpaSecurityJpaConfig;
import fr.openwide.core.showcase.core.business.ShowcaseCoreBusinessPackage;

@Configuration
@EnableAspectJAutoProxy
public class ShowcaseCoreJpaConfig extends AbstractConfiguredJpaSecurityJpaConfig {

	/**
	 * Déclaration des packages de scan pour l'application.
	 */
	@Override
	@Bean
	public JpaPackageScanProvider applicationJpaPackageScanProvider() {
		return new JpaPackageScanProvider(ShowcaseCoreBusinessPackage.class.getPackage());
	}

	@Override
	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
		return super.entityManagerFactory();
	}

	@Override
	@Bean
	public DataSource dataSource() {
		return super.dataSource();
	}

	@Bean
	public JpaPackageScanProvider jpaSecurityPackageScanProvider() {
		return new JpaPackageScanProvider(JpaSecurityBusinessPackage.class.getPackage());
	}

	@Bean
	public JpaPackageScanProvider jpaMorePackageScanProvider() {
		return new JpaPackageScanProvider(JpaMoreBusinessPackage.class.getPackage());
	}

}
