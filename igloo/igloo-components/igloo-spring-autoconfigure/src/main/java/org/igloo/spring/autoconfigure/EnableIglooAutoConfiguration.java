package org.igloo.spring.autoconfigure;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

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
