package test.jpa.more.config.spring;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.iglooproject.config.bootstrap.spring.ExtendedApplicationContextInitializer;
import org.iglooproject.jpa.more.business.CoreJpaMoreBusinessPackage;
import org.iglooproject.test.jpa.junit.JpaOnlyTestConfiguration;
import org.iglooproject.test.jpa.junit.JpaSearchTestConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import igloo.jpa.batch.spring.IglooJpaBatchConfiguration;
import test.jpa.more.business.JpaMoreTestBusinessPackage;

@Retention(RUNTIME)
@Target(TYPE)
@EnableAutoConfiguration
@ContextConfiguration(initializers = ExtendedApplicationContextInitializer.class)
@SpringBootTest(classes = { JpaOnlyTestConfiguration.class, IglooJpaBatchConfiguration.class, JpaSearchTestConfiguration.class, JpaMoreTestApplicationPropertyConfig.class })
@EntityScan(basePackageClasses = { JpaMoreTestBusinessPackage.class, CoreJpaMoreBusinessPackage.class })
public @interface SpringBootTestJpaMoreBatchSearch {

}
