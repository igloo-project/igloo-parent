package org.iglooproject.config.bootstrap.spring.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.PropertySource;

/**
 * @deprecated deprecated in favor of @{@link PropertySource} name from {@link IglooPropertySourcePriority}.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Deprecated
public @interface ConfigurationLocations {

	/**
	 * La liste des « locations » à utiliser pour le chargement des ressources.
	 */
	String[] locations() default {
		"classpath:igloo-component-spring.properties",
		"classpath:igloo-component-jpa.properties",
		"classpath:configuration.properties",
	};

	/**
	 * Permet d'indiquer un ordre de chargement. Les groupes de « locations » chargés en premier sont ceux dont le
	 * champ {@link ConfigurationLocations#order()} est le plus faible. Les définitions de propriétés ayant la plus
	 * grande précédence sont les dernières chargées.
	 * 
	 * <p>Nota : Les « locations » indiquées dans le champ
	 * {@link ConfigurationLocations#locations()} sont chargées dans l'ordre du champ.</p>
	 */
	int order() default Integer.MIN_VALUE;

}
