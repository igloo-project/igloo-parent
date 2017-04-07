package fr.openwide.core.basicapp.core.config.spring;

<<<<<<< 59eeb5a4c7aafc17e1e052a3295ec5412925cc9d
import javax.sql.DataSource;

import org.flywaydb.core.Flyway;

import org.springframework.beans.factory.annotation.Autowired;
||||||| merged common ancestors
=======
import javax.sql.DataSource;

import org.flywaydb.core.Flyway;

import org.springframework.beans.factory.annotation.Autowired;
>>>>>>> add flywaydb dependencies and properties
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

import fr.openwide.core.basicapp.core.business.BasicApplicationCoreCommonBusinessPackage;
import fr.openwide.core.basicapp.core.config.hibernate.HibernateConfigPackage;
import fr.openwide.core.basicapp.core.config.util.FlywayConfiguration;
import fr.openwide.core.jpa.config.spring.provider.JpaPackageScanProvider;
import fr.openwide.core.jpa.more.config.util.FlywayConfiguration;
import fr.openwide.core.jpa.security.config.spring.AbstractConfiguredJpaSecurityJpaConfig;

@Configuration
@EnableAspectJAutoProxy
public class BasicApplicationCoreCommonJpaConfig extends AbstractConfiguredJpaSecurityJpaConfig {

	@Bean
	public FlywayConfiguration flywayConfiguration() {
		return new FlywayConfiguration();
	}

	@Bean(initMethod="migrate")
	@Autowired
	public Flyway flyway(DataSource dataSource, FlywayConfiguration flywayConfiguration) {
		Flyway flyway = new Flyway();
		flyway.setDataSource(dataSource);
		flyway.setSchemas(flywayConfiguration.getSchemas()); 
		flyway.setTable(flywayConfiguration.getTable());
		flyway.setLocations(flywayConfiguration.getLocations());
		flyway.migrate();
		return flyway;
	}
	
	@Override
	@DependsOn(value = "flyway")
	public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
		return super.entityManagerFactory();
	}
	
	/**
	 * Déclaration des packages de scan pour l'application.
	 */
	@Override
	@Bean
	public JpaPackageScanProvider applicationJpaPackageScanProvider() {
		return new JpaPackageScanProvider(
				BasicApplicationCoreCommonBusinessPackage.class.getPackage(),
				HibernateConfigPackage.class.getPackage() // Typedef config
		);
	}
}
