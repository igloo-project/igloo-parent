package org.iglooproject.spring.util;

import java.lang.annotation.Annotation;
import java.util.LinkedHashSet;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.type.filter.AssignableTypeFilter;
import org.springframework.core.type.filter.TypeFilter;

public final class ReflectionUtils {

  private static final Logger LOGGER = LoggerFactory.getLogger(ReflectionUtils.class);

  public static <T> Set<Class<? extends T>> findAssignableClasses(
      String rootPackage, Class<T> clazz) {
    return findClasses(rootPackage, clazz, new AssignableTypeFilter(clazz));
  }

  public static Set<Class<? extends Object>> findAnnotatedClasses(
      String rootPackage, Class<? extends Annotation> annotationType) {
    return findClasses(rootPackage, Object.class, new AnnotationTypeFilter(annotationType));
  }

  @SuppressWarnings("unchecked")
  private static <T> Set<Class<? extends T>> findClasses(
      String rootPackage, Class<T> clazz, TypeFilter filter) {
    ClassPathScanningCandidateComponentProvider scanner =
        new ClassPathScanningCandidateComponentProvider(false);
    scanner.addIncludeFilter(filter);

    Set<BeanDefinition> beanDefinitions = scanner.findCandidateComponents(rootPackage);
    Set<Class<? extends T>> classes = new LinkedHashSet<>();

    for (BeanDefinition beanDefinition : beanDefinitions) {
      try {
        classes.add((Class<? extends T>) Class.forName(beanDefinition.getBeanClassName()));
      } catch (ClassNotFoundException e) {
        LOGGER.warn("Class not found: " + beanDefinition.getBeanClassName());
      }
    }

    return classes;
  }

  private ReflectionUtils() {}
}
