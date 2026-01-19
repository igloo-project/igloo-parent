package org.iglooproject.jpa.security.autoconfigure;

import org.iglooproject.jpa.security.business.JpaSecurityBusinessPackage;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EntityScan(basePackageClasses = JpaSecurityBusinessPackage.class)
public class SecurityModelAutoConfiguration {}
