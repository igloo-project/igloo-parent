package org.iglooproject.config.bootstrap.spring;

import org.iglooproject.config.bootstrap.spring.annotations.IglooPropertySourcesLevels;
import org.iglooproject.config.bootstrap.spring.config.BootstrapPropertySourcesConfiguration;
import org.iglooproject.config.bootstrap.spring.config.PropertySourcesLevelsConfiguration;
import org.iglooproject.config.bootstrap.spring.env.CompositeProtocolResolver;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigRegistry;
import org.springframework.context.annotation.PropertySource;

/**
 * Loading of all {@link IglooPropertySourcesLevels} really early during context loading, to allow
 * convenient {@link PropertySource} ordering, by including {@link
 * PropertySourcesLevelsConfiguration}.
 */
public class ExtendedApplicationContextInitializer
    implements IApplicationContextInitializer,
        ApplicationContextInitializer<ConfigurableApplicationContext> {

  @Override
  public void initialize(ConfigurableApplicationContext applicationContext) {
    applicationContext.addProtocolResolver(new CompositeProtocolResolver());
    // add a configuration to preload all igloo @PropertySource levels
    // (used to control property overriding)
    registerIglooPropertySourcesLevels(applicationContext);
  }

  private void registerIglooPropertySourcesLevels(
      ConfigurableApplicationContext applicationContext) {
    registerBean(
        applicationContext,
        PropertySourcesLevelsConfiguration.class.getSimpleName(),
        PropertySourcesLevelsConfiguration.class);
    registerBean(
        applicationContext,
        BootstrapPropertySourcesConfiguration.class.getSimpleName(),
        BootstrapPropertySourcesConfiguration.class);
  }

  protected void registerBean(
      ConfigurableApplicationContext applicationContext, String beanName, Class<?> beanClass) {
    if (applicationContext instanceof BeanDefinitionRegistry beanDefinitionRegistry) {
      // GenericApplicationContext, AnnotationConfigApplicationContext : unit-test, cli cases
      RootBeanDefinition def2 = new RootBeanDefinition(beanClass);
      def2.setSource(null);
      registerBean(beanDefinitionRegistry, def2, beanName);
    } else if (applicationContext instanceof AnnotationConfigRegistry annotationConfigRegistry) {
      // AnnotationConfigWebApplicationContext : servlet case
      annotationConfigRegistry.register(beanClass);
    }
  }

  private static void registerBean(
      BeanDefinitionRegistry registry, RootBeanDefinition definition, String beanName) {
    definition.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
    registry.registerBeanDefinition(beanName, definition);
  }
}
