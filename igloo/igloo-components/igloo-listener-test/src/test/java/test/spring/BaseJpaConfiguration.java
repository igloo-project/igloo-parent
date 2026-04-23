package test.spring;

import igloo.test.listener.postgresql.PsqlTestContainerConfiguration;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.test.context.ContextConfiguration;
import test.model.DummyEntity;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@EnableAutoConfiguration
@ContextConfiguration(
    classes = {EventRecorderConfiguration.class, PsqlTestContainerConfiguration.class})
@EntityScan(basePackageClasses = DummyEntity.class)
public @interface BaseJpaConfiguration {}
