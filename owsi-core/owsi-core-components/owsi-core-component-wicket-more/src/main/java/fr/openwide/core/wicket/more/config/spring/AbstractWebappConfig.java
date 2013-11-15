package fr.openwide.core.wicket.more.config.spring;

import org.apache.wicket.protocol.http.WebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import fr.openwide.core.jpa.more.config.spring.JpaMoreTaskManagementConfig;
import fr.openwide.core.wicket.more.link.service.DefaultLinkParameterConversionService;
import fr.openwide.core.wicket.more.link.service.ILinkParameterConversionService;
import fr.openwide.core.wicket.more.notification.service.HtmlNotificationCssServiceImpl;
import fr.openwide.core.wicket.more.notification.service.IHtmlNotificationCssService;

@Configuration
@Import({
	WicketMoreServiceConfig.class, 
	JpaMoreTaskManagementConfig.class 
})
public abstract class AbstractWebappConfig {

	@Bean
	public abstract WebApplication application();
	
	@Bean
	public ILinkParameterConversionService linkParameterConversionService() {
		return new DefaultLinkParameterConversionService();
	}
	
	@Bean
	public IHtmlNotificationCssService htmlNotificationCssService() {
		return new HtmlNotificationCssServiceImpl();
	}

}
