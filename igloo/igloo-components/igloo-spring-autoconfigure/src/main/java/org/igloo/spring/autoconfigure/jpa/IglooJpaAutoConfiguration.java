package org.igloo.spring.autoconfigure.jpa;


import org.iglooproject.jpa.config.spring.IglooJpaConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

import jakarta.persistence.EntityManager;

@Configuration
@ConditionalOnClass({ LocalContainerEntityManagerFactoryBean.class, EntityManager.class })
@Import(IglooJpaConfiguration.class)
public class IglooJpaAutoConfiguration {

}
