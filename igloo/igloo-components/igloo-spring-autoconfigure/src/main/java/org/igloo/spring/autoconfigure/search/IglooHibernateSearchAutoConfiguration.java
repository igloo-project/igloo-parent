package org.igloo.spring.autoconfigure.search;

import org.igloo.spring.autoconfigure.jpa.IglooJpaAutoConfiguration;
import org.iglooproject.jpa.search.CoreJpaSearchPackage;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(
	basePackageClasses = {
			CoreJpaSearchPackage.class
	}
)
@AutoConfigureAfter(IglooJpaAutoConfiguration.class)
public class IglooHibernateSearchAutoConfiguration {
	
}
