package org.igloo.spring.autoconfigure.security;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty("security.runAsKey")
@EnableConfigurationProperties(SecurityProperties.class)
public class IglooJpaSecurityRunAsConfig {}
