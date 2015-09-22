package fr.openwide.core.basicapp.web.application.config.spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import fr.openwide.core.basicapp.core.config.spring.BasicApplicationCoreCommonConfig;
import fr.openwide.core.basicapp.web.application.BasicApplicationApplication;
import fr.openwide.core.basicapp.web.application.common.template.styles.notification.NotificationLessCssResourceReference;
import fr.openwide.core.jpa.exception.ServiceException;
import fr.openwide.core.wicket.more.config.spring.AbstractWebappConfig;
import fr.openwide.core.wicket.more.notification.service.IHtmlNotificationCssService;

@Configuration
@Import({
	BasicApplicationCoreCommonConfig.class,
	BasicApplicationWebappSecurityConfig.class,
	BasicApplicationWebappCacheConfig.class
})
@ComponentScan(
		basePackageClasses = {
				BasicApplicationApplication.class
		},
		excludeFilters = @Filter(Configuration.class)
)
public class BasicApplicationWebappConfig extends AbstractWebappConfig {
	
	public static final String DEFAULT_NOTIFICATION_VARIATION = "notification";

	@Override
	@Bean(name= { "BasicApplicationApplication", "application" })
	public BasicApplicationApplication application() {
		return new BasicApplicationApplication();
	}
	
	@Override
	@Bean
	public IHtmlNotificationCssService htmlNotificationCssService() throws ServiceException {
		IHtmlNotificationCssService service = super.htmlNotificationCssService();
		service.registerStyles(DEFAULT_NOTIFICATION_VARIATION, NotificationLessCssResourceReference.get());
		return service;
	}
}
