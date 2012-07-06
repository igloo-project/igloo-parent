package fr.openwide.core.jpa.config.spring;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.aop.Advisor;
import org.springframework.context.annotation.Bean;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import fr.openwide.core.jpa.CoreJpaPackage;
import fr.openwide.core.jpa.config.spring.provider.JpaPackageScanProvider;

public abstract class AbstractJpaConfig {

	@Bean
	public abstract LocalContainerEntityManagerFactoryBean entityManagerFactory();

	@Bean
	public abstract DataSource dataSource();

	@Bean
	public abstract JpaPackageScanProvider applicationJpaPackageScanProvider();

	@Bean
	public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
		return new JpaTransactionManager(entityManagerFactory);
	}

	@Bean
	public Advisor transactionAdvisor(PlatformTransactionManager transactionManager) {
		return JpaConfigUtils.defaultTransactionAdvisor(transactionManager);
	}

	@Bean
	public JpaPackageScanProvider coreJpaPackageScanProvider() {
		return new JpaPackageScanProvider(CoreJpaPackage.class.getPackage());
	}

}
