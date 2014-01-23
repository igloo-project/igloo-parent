package fr.openwide.core.wicket.more.config.spring;

import org.apache.wicket.protocol.http.WebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import fr.openwide.core.jpa.exception.ServiceException;
import fr.openwide.core.wicket.more.link.service.DefaultLinkParameterConversionService;
import fr.openwide.core.wicket.more.link.service.ILinkParameterConversionService;
import fr.openwide.core.wicket.more.notification.service.IHtmlNotificationCssService;
import fr.openwide.core.wicket.more.notification.service.PhlocCssHtmlNotificationCssServiceImpl;

@Configuration
@Import({
	WicketMoreServiceConfig.class
})
public abstract class AbstractWebappConfig {

	@Bean
	public abstract WebApplication application();
	
	@Bean
	public ILinkParameterConversionService linkParameterConversionService() {
		return new DefaultLinkParameterConversionService();
	}
	
	@Bean
	public IHtmlNotificationCssService htmlNotificationCssService() throws ServiceException {
		return new PhlocCssHtmlNotificationCssServiceImpl();
	}

}
