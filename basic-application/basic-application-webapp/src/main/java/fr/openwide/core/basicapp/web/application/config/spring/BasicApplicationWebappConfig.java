package fr.openwide.core.basicapp.web.application.config.spring;

import java.util.Date;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import fr.openwide.core.basicapp.core.business.user.model.BasicUser;
import fr.openwide.core.basicapp.core.business.user.model.TechnicalUser;
import fr.openwide.core.basicapp.core.business.user.model.User;
import fr.openwide.core.basicapp.core.config.spring.BasicApplicationCoreCommonConfig;
import fr.openwide.core.basicapp.web.application.BasicApplicationApplication;
import fr.openwide.core.basicapp.web.application.common.renderer.UserRenderer;
import fr.openwide.core.basicapp.web.application.common.template.styles.notification.NotificationLessCssResourceReference;
import fr.openwide.core.jpa.exception.ServiceException;
import fr.openwide.core.jpa.more.rendering.service.IRendererService;
import fr.openwide.core.wicket.more.config.spring.AbstractWebappConfig;
import fr.openwide.core.wicket.more.notification.model.IWicketNotificationDescriptor;
import fr.openwide.core.wicket.more.notification.service.IHtmlNotificationCssService;
import fr.openwide.core.wicket.more.notification.service.IWicketContextExecutor;
import fr.openwide.core.wicket.more.rendering.BooleanRenderer;
import fr.openwide.core.wicket.more.rendering.Renderer;
import fr.openwide.core.wicket.more.rendering.service.RendererServiceImpl;
import fr.openwide.core.wicket.more.util.DatePattern;

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
public class BasicApplicationWebappConfig extends AbstractWebappConfig {

	@Override
	@Bean(name = { "BasicApplicationApplication", "application" })
	public BasicApplicationApplication application() {
		return new BasicApplicationApplication();
	}
	
	@Override
	public IRendererService rendererService(IWicketContextExecutor wicketContextExecutor) {
		RendererServiceImpl rendererService = new RendererServiceImpl(wicketContextExecutor);

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
