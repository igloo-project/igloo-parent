package org.iglooproject.test.config.spring;

import org.iglooproject.test.business.JpaTestBusinessPackage;
import org.iglooproject.test.jpa.junit.JpaOnlyTestConfiguration;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(JpaOnlyTestConfiguration.class)
@ComponentScan(basePackageClasses = JpaTestBusinessPackage.class)
@EntityScan(basePackageClasses = JpaTestBusinessPackage.class)
public class TestConfiguration {}
