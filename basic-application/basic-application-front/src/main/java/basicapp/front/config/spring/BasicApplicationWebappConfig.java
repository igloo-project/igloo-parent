package basicapp.front.config.spring;

import basicapp.back.business.notification.service.IBasicApplicationNotificationContentDescriptorFactory;
import basicapp.back.business.notification.service.IBasicApplicationNotificationUrlBuilderService;
import basicapp.back.business.user.model.User;
import basicapp.back.config.spring.BasicApplicationCoreCommonConfiguration;
import basicapp.front.BasicApplicationApplication;
import basicapp.front.notification.service.BasicApplicationNotificationContentDescriptorFactoryImpl;
import basicapp.front.notification.service.BasicApplicationNotificationUrlBuilderServiceImpl;
import basicapp.front.user.renderer.UserRenderer;
import igloo.julhelper.servlet.JakartaJulLoggingListener;
import igloo.log4j2jmx.servlet.JakartaLog4j2LoggingManagerListener;
import igloo.wicket.servlet.filter.Log4jUrlFilter;
import jakarta.servlet.DispatcherType;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.SessionTrackingMode;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.EnumSet;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.protocol.http.WicketFilter;
import org.iglooproject.jpa.exception.ServiceException;
import org.iglooproject.jpa.more.rendering.service.IRendererService;
import org.iglooproject.wicket.more.notification.service.IHtmlNotificationCssService;
import org.iglooproject.wicket.more.notification.service.IWicketContextProvider;
import org.iglooproject.wicket.more.notification.service.PhlocCssHtmlNotificationCssServiceImpl;
import org.iglooproject.wicket.more.rendering.BooleanRenderer;
import org.iglooproject.wicket.more.rendering.service.RendererServiceImpl;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.Ordered;
import org.springframework.orm.jpa.support.OpenEntityManagerInViewFilter;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@Configuration
@EnableWebSecurity
@Import({
  BasicApplicationCoreCommonConfiguration.class,
  BasicApplicationWebappApplicationPropertyRegistryConfiguration.class,
  BasicApplicationSecurityConfiguration.class
})
@ComponentScan(
    basePackageClasses = {BasicApplicationApplication.class},
    excludeFilters = @Filter(Configuration.class))
public class BasicApplicationWebappConfig {

  @Bean(name = {"BasicApplicationApplication", "application"})
  public BasicApplicationApplication application() {
    return new BasicApplicationApplication();
  }

  @Bean
  public IRendererService rendererService(IWicketContextProvider wicketContextProvider) {
    RendererServiceImpl rendererService = new RendererServiceImpl(wicketContextProvider);

    rendererService.registerRenderer(Boolean.class, BooleanRenderer.get());
    rendererService.registerRenderer(boolean.class, BooleanRenderer.get());

    rendererService.registerRenderer(User.class, UserRenderer.get());

    return rendererService;
  }

  /** Override parent bean declaration so that we add our custom styles. */
  @Bean
  public IHtmlNotificationCssService htmlNotificationCssService() throws ServiceException {
    IHtmlNotificationCssService service = new PhlocCssHtmlNotificationCssServiceImpl();
    //    service.registerDefaultStyles(NotificationEmailScssResourceReference.get());
    return service;
  }

  @Configuration
  public static class WebXmlConfiguration {
    private static final EnumSet<DispatcherType> allDispatcherTypes =
        EnumSet.of(DispatcherType.REQUEST, DispatcherType.FORWARD, DispatcherType.INCLUDE);

    private int log4jUrlFilterPrecedence = Ordered.HIGHEST_PRECEDENCE;
    private int openEntityManagerInViewFilterPrecedence = Ordered.HIGHEST_PRECEDENCE + 1;

    private int wicketFilterPrecedence = Ordered.LOWEST_PRECEDENCE;

    @Bean
    public FilterRegistrationBean<Log4jUrlFilter> log4jUrlFilter() {
      FilterRegistrationBean<Log4jUrlFilter> bean = new FilterRegistrationBean<>();
      bean.setFilter(new Log4jUrlFilter());
      bean.setOrder(log4jUrlFilterPrecedence);
      bean.setDispatcherTypes(allDispatcherTypes);
      return bean;
    }

    @Bean
    public FilterRegistrationBean<OpenEntityManagerInViewFilter> openEntityManagerInViewFilter() {
      FilterRegistrationBean<OpenEntityManagerInViewFilter> bean = new FilterRegistrationBean<>();
      bean.setFilter(new OpenEntityManagerInViewFilter());
      bean.setOrder(openEntityManagerInViewFilterPrecedence);
      bean.setDispatcherTypes(allDispatcherTypes);
      return bean;
    }

    @Bean
    public FilterRegistrationBean<WicketFilter> wicketFilter(WebApplication application) {
      FilterRegistrationBean<WicketFilter> bean = new FilterRegistrationBean<>();
      bean.setFilter(new WicketFilter(application));
      bean.setOrder(wicketFilterPrecedence);
      bean.setDispatcherTypes(allDispatcherTypes);
      bean.addInitParameter("filterMappingUrlPattern", "/*");

      return bean;
    }

    @Configuration
    public static class CommonInitializer implements ServletContextInitializer {
      @Override
      public void onStartup(ServletContext servletContext) throws ServletException {
        servletContext.setResponseCharacterEncoding(StandardCharsets.UTF_8.displayName());
        servletContext.setRequestCharacterEncoding(StandardCharsets.UTF_8.displayName());
        servletContext.setSessionTimeout(480);
        servletContext.setSessionTrackingModes(Collections.singleton(SessionTrackingMode.COOKIE));
        servletContext.addListener(JakartaJulLoggingListener.class);
        servletContext.addListener(JakartaLog4j2LoggingManagerListener.class);
      }
    }
  }

  @Configuration
  public static class NotificationConfiguration {
    @Bean
    public IBasicApplicationNotificationContentDescriptorFactory contentDescriptorFactory(
        IWicketContextProvider wicketContextProvider) {
      return new BasicApplicationNotificationContentDescriptorFactoryImpl(wicketContextProvider);
    }

    @Bean
    public IBasicApplicationNotificationUrlBuilderService notificationUrlBuilderService(
        IWicketContextProvider wicketContextProvider) {
      return new BasicApplicationNotificationUrlBuilderServiceImpl(wicketContextProvider);
    }
  }
}
