package fr.openwide.core.jpa.config.spring;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.aop.Advisor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import fr.openwide.core.jpa.business.generic.CoreJpaBusinessGenericPackage;
import fr.openwide.core.jpa.config.spring.provider.JpaPackageScanProvider;
import fr.openwide.core.jpa.search.CoreJpaSearchPackage;
import fr.openwide.core.jpa.util.CoreJpaUtilPackage;

/**
 * L'implémentation de cette classe doit être annotée {@link EnableAspectJAutoProxy}
 */
@ComponentScan(
	basePackageClasses = { CoreJpaBusinessGenericPackage.class, CoreJpaSearchPackage.class, CoreJpaUtilPackage.class },
	excludeFilters = @Filter(Configuration.class)
)
public abstract class AbstractJpaConfig {

	@Bean
	public abstract LocalContainerEntityManagerFactoryBean entityManagerFactory();

	/**
	 * Il est important de déterminer la destroyMethod sur l'annotation {@link Bean}. Spring prend en compte
	 * auto-magiquement la méthode close() si présente si pas de configuration.
	 */
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
		return new JpaPackageScanProvider(CoreJpaBusinessGenericPackage.class.getPackage());
	}

}
