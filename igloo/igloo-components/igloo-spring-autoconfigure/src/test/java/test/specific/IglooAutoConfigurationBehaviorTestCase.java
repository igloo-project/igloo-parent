package test.specific;

import static org.assertj.core.api.Assertions.assertThat;

import org.igloo.spring.autoconfigure.EnableIglooAutoConfiguration;
import org.igloo.spring.autoconfigure.applicationconfig.IglooApplicationConfigAutoConfiguration;
import org.igloo.spring.autoconfigure.bootstrap.IglooBootstrap4AutoConfiguration;
import org.igloo.spring.autoconfigure.security.IglooJpaSecurityAutoConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration;
import org.springframework.boot.autoconfigure.gson.GsonAutoConfiguration;
import org.springframework.boot.autoconfigure.mail.MailSenderAutoConfiguration;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSenderImpl;

/**
 * Base class used to check that {@link EnableIglooAutoConfiguration} triggers only Igloo
 * auto-configuration, without Spring Boot auto-configuration. Tests are :
 *
 * <ul>
 *   <li>Both configuration behavior
 *   <li>Spring Boot alone behavior, as a reference behavior (test for Java MailSender bean)
 *   <li>Igloo auto-configuration behavior
 * </ul>
 *
 * This class uses ApplicationContextRunner to initialize contexts with suitable configurations,
 * which are declared at the bottom of the file.
 *
 * <p>This test relies on the fact that javax.mail and spring-mail is available on classpath.
 */
class IglooAutoConfigurationBehaviorTestCase {

  /**
   * Check that autoconfiguration from {@link MailSenderAutoConfiguration} is triggered normally as
   * we use here both Spring Boot {@link EnableAutoConfiguration} and IglooAutoConfigure.
   */
  @Test
  void testBothSpringBootIglooAutoConfigure() {
    new ApplicationContextRunner()
        .withAllowBeanDefinitionOverriding(true)
        .withPropertyValues("spring.mail.host=localhost")
        .withConfiguration(AutoConfigurations.of(TestBothSpringBootIglooConfig.class))
        .run(context -> assertThat(context).hasSingleBean(JavaMailSenderImpl.class));
  }

  /**
   * Check that autoconfiguration from {@link MailSenderAutoConfiguration} is triggered normally as
   * we use here Spring Boot {@link EnableAutoConfiguration}.
   */
  @Test
  void testSpringBootAutoConfigure() {
    new ApplicationContextRunner()
        .withAllowBeanDefinitionOverriding(true)
        .withPropertyValues(
            "spring.mail.host=localhost",
            // disabled by default in igloo; allow to get rid of jaxb
            "spring.jpa.properties.hibernate.xml_mapping_enabled=false",
            // we need hibernate 5.5 for default naming strategy; see HibernateProperties
            "spring.jpa.hibernate.naming.physicalStrategy=org.springframework.boot.orm.jpa.hibernate.SpringPhysicalNamingStrategy")
        .withConfiguration(AutoConfigurations.of(TestSpringBootConfig.class))
        .run(context -> assertThat(context).hasSingleBean(JavaMailSenderImpl.class));
  }

  /**
   * Check that autoconfiguration from {@link MailSenderAutoConfiguration} is not triggered despite
   * spring.mail.host setting as we want to inhibit all Spring Boot autoconfiguration
   */
  @Test
  void testIglooAutoConfigure() {
    new ApplicationContextRunner()
        .withAllowBeanDefinitionOverriding(true)
        .withPropertyValues("spring.mail.host=localhost")
        .withConfiguration(AutoConfigurations.of(TestIglooConfig.class))
        .run(context -> assertThat(context).doesNotHaveBean(JavaMailSenderImpl.class));
  }

  @Configuration
  @EnableAutoConfiguration(exclude = GsonAutoConfiguration.class)
  @EnableIglooAutoConfiguration(
      exclude = {
        IglooBootstrap4AutoConfiguration.class,
        IglooJpaSecurityAutoConfiguration.class,
        IglooApplicationConfigAutoConfiguration.class
      })
  public static class TestBothSpringBootIglooConfig {}

  @Configuration
  // our gson version is not compatible with spring-boot
  @EnableAutoConfiguration(exclude = {FlywayAutoConfiguration.class, GsonAutoConfiguration.class})
  public static class TestSpringBootConfig {}

  @Configuration
  @EnableIglooAutoConfiguration(
      exclude = {
        IglooBootstrap4AutoConfiguration.class,
        IglooJpaSecurityAutoConfiguration.class,
        IglooApplicationConfigAutoConfiguration.class
      })
  public static class TestIglooConfig {}
}
