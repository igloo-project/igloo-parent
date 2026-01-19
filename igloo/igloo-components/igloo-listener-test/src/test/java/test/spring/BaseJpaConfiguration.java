package test.spring;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import igloo.test.listener.postgresql.PsqlTestContainerConfiguration;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.test.context.ContextConfiguration;
import test.model.DummyEntity;

@EnableAutoConfiguration
@ContextConfiguration(
    classes = {EventRecorderConfiguration.class, PsqlTestContainerConfiguration.class})
@EntityScan(basePackageClasses = DummyEntity.class)
@Retention(RUNTIME)
@Target(TYPE)
public @interface BaseJpaConfiguration {}
