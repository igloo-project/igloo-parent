package org.iglooproject.test.config.spring;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.iglooproject.config.bootstrap.spring.ExtendedTestApplicationContextInitializer;
import org.iglooproject.test.jpa.junit.JpaCacheTestConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

/**
 * Tests with batch and hibernate search
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@EnableAutoConfiguration
@SpringBootTest(classes = { TestConfiguration.class, JpaCacheTestConfiguration.class})
@ContextConfiguration(initializers = ExtendedTestApplicationContextInitializer.class)
public @interface SpringBootTestJpaCache {

}
