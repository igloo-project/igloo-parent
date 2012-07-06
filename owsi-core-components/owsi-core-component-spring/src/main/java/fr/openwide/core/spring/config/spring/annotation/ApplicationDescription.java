package fr.openwide.core.spring.config.spring.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ApplicationDescription {

	/**
	 * Le nom de l'application ; ce nom est utilisé pour générer les chemins pour la configuration de l'application.
	 * Il est nécessaire d'indiquer un nom compatible avec le système de fichier utilisé.
	 */
	String name() default "";

}
