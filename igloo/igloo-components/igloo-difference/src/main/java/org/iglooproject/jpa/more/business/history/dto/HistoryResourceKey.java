package org.iglooproject.jpa.more.business.history.dto;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Translation key segment of an entity, used to display its history differences.
 *
 * <p>The way the segment is used (translation keys lookup, default segment when the entity is not
 * annotated) is up to the project. The usual convention is to use the simple name of the class in
 * lowerCamelCase (e.g. {@code Dossier} → {@code dossier}) by default: the annotation is then only
 * needed for the entities whose keys do not follow this convention.
 *
 * <p>Example: {@code @HistoryResourceKey("dossier.commerce")} on {@code DossierCommerce}, whose
 * keys are {@code business.dossier.commerce.*}.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface HistoryResourceKey {

  /** Key segment, without leading dot (e.g. {@code dossier.commerce}). */
  String value();
}
