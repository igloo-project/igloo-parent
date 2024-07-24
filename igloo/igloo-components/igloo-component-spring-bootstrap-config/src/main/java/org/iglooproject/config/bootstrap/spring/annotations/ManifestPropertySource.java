package org.iglooproject.config.bootstrap.spring.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.context.annotation.Configuration;

/**
 * {@link Configuration} class marker to trigger MANIFEST property loading.
 *
 * <p>
 *
 * <p>All properties are prefixed by the provided prefix and can be targetted with placeholders:
 *
 * <pre>
 * version=${prefix.Implementation-Version}
 * </pre>
 *
 * <p>To load MANIFEST info from an jar, provide an alternate class with clazz or className
 * attribute.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Repeatable(ManifestPropertySources.class)
public @interface ManifestPropertySource {

  /** Used prefix to access manifest properties. */
  String prefix() default "";

  /** If not provided, jar is determined from annotated class. Else use the provided class. */
  Class<?> clazz() default void.class;

  /** Fallback if no class loaded. */
  String className() default "";

  /** Stop context loading if class cannot be found. */
  boolean failIfNotFound() default true;
}
