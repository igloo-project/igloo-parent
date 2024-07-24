package test.wicket.more.config.spring;

import org.iglooproject.spring.autoconfigure.SpringPropertyRegistryAutoConfiguration;
import org.iglooproject.test.jpa.junit.JpaOnlyTestConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import test.wicket.more.business.WicketMoreTestBusinessPackage;

/** Stub. */
@Configuration
@Import({JpaOnlyTestConfiguration.class, SpringPropertyRegistryAutoConfiguration.class})
@ComponentScan(basePackageClasses = WicketMoreTestBusinessPackage.class)
@EntityScan(basePackageClasses = WicketMoreTestBusinessPackage.class)
public class TestCommonConfiguration {}
