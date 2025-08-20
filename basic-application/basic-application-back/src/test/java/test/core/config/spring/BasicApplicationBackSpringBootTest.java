package test.core.config.spring;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import org.iglooproject.config.bootstrap.spring.ExtendedApplicationContextInitializer;
import org.iglooproject.test.jpa.junit.EntityManagerExecutionListener;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.TestExecutionListeners.MergeMode;
import org.springframework.test.context.TestPropertySource;

@Retention(RUNTIME)
@Target(TYPE)
@SpringBootTest(classes = BasicApplicationBackSpringBootTestConfiguration.class)
@ContextConfiguration(initializers = ExtendedApplicationContextInitializer.class)
@TestExecutionListeners(
    listeners = EntityManagerExecutionListener.class,
    mergeMode = MergeMode.MERGE_WITH_DEFAULTS)
@TestPropertySource(properties = {"igloo.profile=test"})
public @interface BasicApplicationBackSpringBootTest {}
