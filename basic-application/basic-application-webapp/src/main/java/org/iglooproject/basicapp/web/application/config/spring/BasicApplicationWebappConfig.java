package org.iglooproject.basicapp.web.application.config.spring;

import java.util.Date;

import org.iglooproject.basicapp.core.business.user.model.BasicUser;
import org.iglooproject.basicapp.core.business.user.model.TechnicalUser;
import org.iglooproject.basicapp.core.business.user.model.User;
import org.iglooproject.basicapp.core.config.spring.BasicApplicationCoreCommonConfig;
import org.iglooproject.basicapp.web.application.BasicApplicationApplication;
import org.iglooproject.basicapp.web.application.common.renderer.UserRenderer;
import org.iglooproject.basicapp.web.application.common.template.styles.old.notification.NotificationLessCssResourceReference;
import org.iglooproject.jpa.exception.ServiceException;
import org.iglooproject.jpa.more.rendering.service.IRendererService;
import org.iglooproject.wicket.bootstrap4.config.spring.AbstractBootstrapWebappConfig;
import org.iglooproject.wicket.more.notification.model.IWicketNotificationDescriptor;
import org.iglooproject.wicket.more.notification.service.IHtmlNotificationCssService;
import org.iglooproject.wicket.more.notification.service.IWicketContextProvider;
import org.iglooproject.wicket.more.rendering.BooleanRenderer;
import org.iglooproject.wicket.more.rendering.Renderer;
import org.iglooproject.wicket.more.rendering.service.RendererServiceImpl;
import org.iglooproject.wicket.more.util.DatePattern;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
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
public class BasicApplicationWebappConfig extends AbstractBootstrapWebappConfig {

	@Override
	@Bean(name = { "BasicApplicationApplication", "application" })
	public BasicApplicationApplication application() {
		return new BasicApplicationApplication();
	}
	
	@Override
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

	@Override
	@Bean
	public IHtmlNotificationCssService htmlNotificationCssService() throws ServiceException {
		IHtmlNotificationCssService service = super.htmlNotificationCssService();
		service.registerStyles(IWicketNotificationDescriptor.DEFAULT_NOTIFICATION_VARIATION, NotificationLessCssResourceReference.get());
		return service;
	}

}
