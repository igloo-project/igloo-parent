package org.igloo.spring.autoconfigure;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.springframework.boot.autoconfigure.AutoConfigurationImportSelector;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.io.support.SpringFactoriesLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.Assert;

public class IglooAutoConfigurationImportSelector extends AutoConfigurationImportSelector {

  public static final String PROPERTY_NAME_AUTOCONFIGURE_EXCLUDE = "igloo.autoconfigure.exclude";

  @Override
  protected Class<?> getAnnotationClass() {
    return EnableIglooAutoConfiguration.class;
  }

  @Override
  protected Class<?> getSpringFactoriesLoaderFactoryClass() {
    return EnableIglooAutoConfiguration.class;
  }

  @Override
  protected List<String> getExcludeAutoConfigurationsProperty() {
    if (getEnvironment() instanceof ConfigurableEnvironment) {
      Binder binder = Binder.get(getEnvironment());
      return binder
          .bind(PROPERTY_NAME_AUTOCONFIGURE_EXCLUDE, String[].class)
          .map(Arrays::asList)
          .orElse(Collections.emptyList());
    }
    String[] excludes =
        getEnvironment().getProperty(PROPERTY_NAME_AUTOCONFIGURE_EXCLUDE, String[].class);
    return (excludes != null) ? Arrays.asList(excludes) : Collections.emptyList();
  }

  /**
   * With spring-boot 2.7, default implementation automatically add spring-boot autoconfig. We want
   * only igloo auto-configuration !
   */
  @Override
  protected List<String> getCandidateConfigurations(
      AnnotationMetadata metadata, AnnotationAttributes attributes) {
    List<String> configurations =
        new ArrayList<>(
            SpringFactoriesLoader.loadFactoryNames(
                getSpringFactoriesLoaderFactoryClass(), getBeanClassLoader()));
    // We skip spring-boot autoconfiguration loading
    // ImportCandidates.load(AutoConfiguration.class,
    // getBeanClassLoader()).forEach(configurations::add);
    Assert.notEmpty(
        configurations,
        "No auto configuration classes found in META-INF/spring.factories nor in META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports. If you "
            + "are using a custom packaging, make sure that file is correct.");
    return configurations;
  }
}
