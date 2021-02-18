package org.igloo.spring.autoconfigure;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.igloo.spring.autoconfigure.applicationconfig.IglooApplicationConfigAutoConfiguration;
import org.igloo.spring.autoconfigure.bootstrap.IglooBootstrap4AutoConfiguration;
import org.igloo.spring.autoconfigure.flyway.IglooFlywayAutoConfiguration;
import org.igloo.spring.autoconfigure.jpa.IglooJpaAutoConfiguration;
import org.igloo.spring.autoconfigure.jpa.IglooJpaMoreAutoConfiguration;
import org.igloo.spring.autoconfigure.property.IglooPropertyAutoConfiguration;
import org.igloo.spring.autoconfigure.search.IglooHibernateSearchAutoConfiguration;
import org.igloo.spring.autoconfigure.security.IglooJpaSecurityAutoConfiguration;
import org.igloo.spring.autoconfigure.task.IglooTaskManagementAutoConfiguration;
import org.igloo.spring.autoconfigure.wicket.IglooWicketAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigurationImportSelector;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Import;

/**
 * Specialized {@link EnableAutoConfiguration} that disables Spring Boot auto configuration by default and do not
 * trigger {@link AutoConfigurationPackage} as we prefer to explicitly trigger package scanning.
 * 
 * Methods {@link #exclude()} and {@link #excludeName()} are from {@link EnableAutoConfiguration}, and needed by
 * {@link AutoConfigurationImportSelector} implementation.
 * 
 * Properties allow to enable/disable autoconfigurations:
 * 
 * <ul>
 * <li>igloo-ac.jpa.disabled: {@link IglooJpaAutoConfiguration}</li>
 * <li>igloo-ac.jpa-more.disabled: {@link IglooJpaMoreAutoConfiguration}</li>
 * <li>igloo-ac.flyway.disabled: {@link IglooFlywayAutoConfiguration}</li>
 * <li>igloo-ac.jpa-security.disabled: {@link IglooJpaSecurityAutoConfiguration}</li>
 * <li>igloo-ac.tasks.disabled: {@link IglooTaskManagementAutoConfiguration}</li>
 * <li>igloo-ac.hsearch.disabled: {@link IglooHibernateSearchAutoConfiguration}</li>
 * <li>igloo-ac.property.disabled: {@link IglooPropertyAutoConfiguration}</li>
 * <li>igloo-ac.application.disabled: {@link IglooApplicationConfigAutoConfiguration}</li>
 * <li>igloo-ac.bootstrap4.disabled: {@link IglooBootstrap4AutoConfiguration}</li>
 * <li>igloo-ac.bootstrap5.disabled: {@link IglooBootstrap5AutoConfiguration}</li>
 * <li>igloo-ac.wicket.disabled: {@link IglooWicketAutoConfiguration}</li>
 * </ul>
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import(IglooAutoConfigurationImportSelector.class)
public @interface EnableIglooAutoConfiguration {

	/**
	 * Exclude specific auto-configuration classes such that they will never be applied.
	 * @return the classes to exclude
	 */
	Class<?>[] exclude() default {};

	/**
	 * Exclude specific auto-configuration class names such that they will never be
	 * applied.
	 * @return the class names to exclude
	 * @since 1.3.0
	 */
	String[] excludeName() default {};

}
