package fr.openwide.core.wicket.more.config.spring;

import org.apache.wicket.protocol.http.WebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import fr.openwide.core.jpa.more.config.spring.JpaMoreTaskManagementConfig;

@Configuration
@Import({
	WicketMoreServiceConfig.class, 
	JpaMoreTaskManagementConfig.class 
})
public abstract class AbstractWebappConfig {

	@Bean
	public abstract WebApplication application();

}
