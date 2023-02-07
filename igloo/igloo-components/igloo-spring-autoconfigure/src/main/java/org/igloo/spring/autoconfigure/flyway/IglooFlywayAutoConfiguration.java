package org.igloo.spring.autoconfigure.flyway;


import org.flywaydb.core.api.migration.JavaMigration;
import org.igloo.spring.autoconfigure.jpa.IglooJpaAutoConfiguration;
import org.igloo.spring.autoconfigure.property.IglooPropertyAutoConfiguration;
import org.iglooproject.config.bootstrap.spring.annotations.IglooPropertySourcePriority;
import org.iglooproject.jpa.config.spring.FlywayPropertyRegistryConfig;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;

@Configuration
@AutoConfigureAfter({IglooPropertyAutoConfiguration.class, IglooJpaAutoConfiguration.class})
@Import({ FlywayPropertyRegistryConfig.class, FlywayAutoConfiguration.class })
@PropertySource(
	name = IglooPropertySourcePriority.COMPONENT,
	value = "classpath:/configuration/flyway-common.properties",
	encoding = "UTF-8"
)
public class IglooFlywayAutoConfiguration {

	/**
	 *  {@link JavaMigration} bean scanning and <code>spring.flyway.locations</code> definition for init-less migration.
	 * 
	 * Active only when migration.init.enabled=false or missing.
	 */
	@Configuration
	@ComponentScan(basePackages = "db.migration.common")
	@PropertySource(
			name = IglooPropertySourcePriority.COMPONENT,
			value = "classpath:/flyway/flyway-without-init.properties",
			encoding = "UTF-8"
		)
	@ConditionalOnProperty(prefix = "migration.init", name = "enabled", havingValue = "false", matchIfMissing = true)
	public static class JavaMigrationWithoutInit {
	}

	/**
	 *  {@link JavaMigration} bean scanning and <code>spring.flyway.locations</code> definition for init-aware migration.
	 *  
	 *  Bean scanning is configured on <code>db.migration</code> package, so that it captures both common and init
	 *  subpackages.
	 * 
	 * Active only when migration.init.enabled=false or missing.
	 */
	@Configuration
	@ComponentScan(basePackages = "db.migration")
	@PropertySource(
		name = IglooPropertySourcePriority.COMPONENT,
		value = "classpath:/flyway/flyway-with-init.properties",
		encoding = "UTF-8"
	)
	@ConditionalOnProperty(prefix = "migration.init", name = "enabled", havingValue = "true", matchIfMissing = false)
	public static class SqlAndJavaMigrationWithInit {
	}

}
