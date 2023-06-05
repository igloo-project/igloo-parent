package test.jpa.more.config.spring;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.iglooproject.config.bootstrap.spring.ExtendedApplicationContextInitializer;
import org.iglooproject.test.jpa.junit.EntityManagerExecutionListener;
import org.iglooproject.test.jpa.junit.JpaOnlyTestConfiguration;
import org.iglooproject.test.jpa.junit.JpaSearchTestConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import igloo.jpa.batch.spring.IglooJpaBatchConfiguration;
import test.jpa.more.business.JpaMoreTestBusinessPackage;

@Retention(RUNTIME)
@Target(TYPE)
@EnableAutoConfiguration
@ContextConfiguration(initializers = ExtendedApplicationContextInitializer.class)
@SpringBootTest(classes = { TestConfig.class, JpaOnlyTestConfiguration.class, IglooJpaBatchConfiguration.class, JpaSearchTestConfiguration.class })
@TestExecutionListeners({
	DependencyInjectionTestExecutionListener.class,
	EntityManagerExecutionListener.class
})
@TestPropertySource(properties = "igloo.profile=test")
@EntityScan(basePackageClasses = { JpaMoreTestBusinessPackage.class })
public @interface SpringBootTestJpaMoreBatchSearch {

}
