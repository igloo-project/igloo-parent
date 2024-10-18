package test.spring;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.test.context.ContextConfiguration;
import test.model.DummyEntity;

@EnableAutoConfiguration
@ContextConfiguration(classes = {EventRecorderConfiguration.class})
@EntityScan(basePackageClasses = DummyEntity.class)
@Retention(RUNTIME)
@Target(TYPE)
public @interface BaseJpaConfiguration {}
