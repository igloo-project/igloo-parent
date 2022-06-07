package org.iglooproject.basicapp.web.application.config.spring;

import java.util.Date;

import org.iglooproject.basicapp.core.business.user.model.BasicUser;
import org.iglooproject.basicapp.core.business.user.model.TechnicalUser;
import org.iglooproject.basicapp.core.business.user.model.User;
import org.iglooproject.basicapp.core.config.spring.BasicApplicationCoreCommonConfig;
import org.iglooproject.basicapp.web.application.BasicApplicationApplication;
import org.iglooproject.basicapp.web.application.common.renderer.UserRenderer;
import org.iglooproject.basicapp.web.application.common.template.resources.styles.notification.NotificationScssResourceReference;
import org.iglooproject.jpa.exception.ServiceException;
import org.iglooproject.jpa.more.rendering.service.IRendererService;
import org.iglooproject.wicket.more.notification.service.IHtmlNotificationCssService;
import org.iglooproject.wicket.more.notification.service.IWicketContextProvider;
import org.iglooproject.wicket.more.notification.service.PhlocCssHtmlNotificationCssServiceImpl;
import org.iglooproject.wicket.more.rendering.BooleanRenderer;
import org.iglooproject.wicket.more.rendering.service.RendererServiceImpl;
import org.iglooproject.wicket.renderer.Renderer;
import org.iglooproject.wicket.util.DatePattern;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@Configuration
@EnableWebSecurity
@Import({
	BasicApplicationCoreCommonConfig.class,
	BasicApplicationWebappSecurityConfig.class,
	BasicApplicationWebappCacheConfig.class,
	BasicApplicationWebappApplicationPropertyRegistryConfig.class
})
@ComponentScan(
	basePackageClasses = {
		BasicApplicationApplication.class
	},
	excludeFilters = @Filter(Configuration.class)
)
public class BasicApplicationWebappConfig {

	@Bean(name = { "BasicApplicationApplication", "application" })
	public BasicApplicationApplication application() {
		return new BasicApplicationApplication();
	}

	@Bean
	public IRendererService rendererService(IWicketContextProvider wicketContextProvider) {
		RendererServiceImpl rendererService = new RendererServiceImpl(wicketContextProvider);
		
		rendererService.registerRenderer(Boolean.class, BooleanRenderer.get());
		rendererService.registerRenderer(boolean.class, BooleanRenderer.get());
		
		Renderer<Date> shortDateRenderer = Renderer.fromDatePattern(DatePattern.SHORT_DATE);
		rendererService.registerRenderer(Date.class, shortDateRenderer);
		rendererService.registerRenderer(java.sql.Date.class, shortDateRenderer);
		
		rendererService.registerRenderer(User.class, UserRenderer.get());
		rendererService.registerRenderer(TechnicalUser.class, UserRenderer.get());
		rendererService.registerRenderer(BasicUser.class, UserRenderer.get());
		
		return rendererService;
	}

	/**
	 * Override parent bean declaration so that we add our custom styles.
	 */
	@Bean
	public IHtmlNotificationCssService htmlNotificationCssService() throws ServiceException {
		IHtmlNotificationCssService service = new PhlocCssHtmlNotificationCssServiceImpl();
		service.registerDefaultStyles(NotificationScssResourceReference.get());
		return service;
	}

}
