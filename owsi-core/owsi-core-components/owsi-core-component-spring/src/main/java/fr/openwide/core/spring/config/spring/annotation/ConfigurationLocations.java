package fr.openwide.core.spring.config.spring.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ConfigurationLocations {

	/**
	 * La liste des « locations » à utiliser pour le chargement des ressources.
	 */
	String[] locations() default {
		"classpath:owsi-core-component-spring.properties",
		"classpath:owsi-core-component-jpa.properties",
		"classpath:configuration-private.properties",
		"classpath:configuration.properties",
		"file:/etc/${applicationName}/configuration.properties",
		"classpath:configuration-${environment}.properties",
		"classpath:configuration-${user}.properties"
	};

	/**
	 * Permet d'indiquer une implémentation alternative chargée de transformer les valeurs du champ
	 * {@link ConfigurationLocations#locations()}.
	 */
	Class<? extends IConfigurationLocationProvider> configurationLocationProvider()
		default DefaultConfigurationLocationProvider.class;

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
