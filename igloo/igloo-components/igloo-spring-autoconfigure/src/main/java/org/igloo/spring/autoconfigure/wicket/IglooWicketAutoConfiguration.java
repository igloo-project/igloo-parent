package org.igloo.spring.autoconfigure.wicket;

import org.apache.wicket.Page;
import org.apache.wicket.protocol.http.WebApplication;
import org.iglooproject.jpa.exception.ServiceException;
import org.iglooproject.jpa.more.rendering.service.IRendererService;
import org.iglooproject.wicket.more.config.spring.WicketMoreApplicationPropertyRegistryConfig;
import org.iglooproject.wicket.more.config.spring.WicketMoreServiceConfig;
import org.iglooproject.wicket.more.link.service.DefaultLinkParameterConversionService;
import org.iglooproject.wicket.more.link.service.ILinkParameterConversionService;
import org.iglooproject.wicket.more.notification.service.IHtmlNotificationCssService;
import org.iglooproject.wicket.more.notification.service.IWicketContextProvider;
import org.iglooproject.wicket.more.notification.service.PhlocCssHtmlNotificationCssServiceImpl;
import org.iglooproject.wicket.more.notification.service.WicketContextProviderImpl;
import org.iglooproject.wicket.more.rendering.service.RendererServiceImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;

@Configuration
@ConditionalOnProperty(
    name = "igloo-ac.wicket.disabled",
    havingValue = "false",
    matchIfMissing = true)
@ConditionalOnClass(WebApplication.class)
@Import({WicketMoreServiceConfig.class, WicketMoreApplicationPropertyRegistryConfig.class})
public class IglooWicketAutoConfiguration {

  @Bean
  @ConditionalOnMissingBean(WebApplication.class)
  public WebApplication webApplication() {
    return new WebApplication() {
      @Override
      public Class<? extends Page> getHomePage() {
        return null;
      }
    };
  }

  @Bean
  /* Use a proxy to fix a circular dependency.
   * There's no real notion of scope here, since the bean is a singleton: we just want it to be proxyfied so that
   * the circular dependency is broken.
   */
  @Scope(proxyMode = ScopedProxyMode.INTERFACES)
  public IWicketContextProvider wicketContextProvider(WebApplication defaultApplication) {
    return new WicketContextProviderImpl(defaultApplication);
  }

  @Bean
  @ConditionalOnMissingBean
  public IRendererService rendererService(IWicketContextProvider wicketContextProvider) {
    return new RendererServiceImpl(wicketContextProvider);
  }

  @Bean
  @ConditionalOnMissingBean
  public ILinkParameterConversionService linkParameterConversionService() {
    return new DefaultLinkParameterConversionService();
  }

  @Bean
  @ConditionalOnMissingBean(IHtmlNotificationCssService.class)
  public IHtmlNotificationCssService htmlNotificationCssService() throws ServiceException {
    return new PhlocCssHtmlNotificationCssServiceImpl();
  }
}
