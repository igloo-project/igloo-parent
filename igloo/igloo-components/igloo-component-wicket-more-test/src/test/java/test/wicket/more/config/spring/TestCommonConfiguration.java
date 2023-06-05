package test.wicket.more.config.spring;

import org.apache.wicket.protocol.http.WebApplication;
import org.iglooproject.jpa.exception.ServiceException;
import org.iglooproject.jpa.more.rendering.service.IRendererService;
import org.iglooproject.spring.autoconfigure.SpringPropertyRegistryAutoConfiguration;
import org.iglooproject.test.jpa.junit.JpaOnlyTestConfiguration;
import org.iglooproject.wicket.more.config.spring.WicketMoreApplicationPropertyRegistryConfig;
import org.iglooproject.wicket.more.config.spring.WicketMoreServiceConfig;
import org.iglooproject.wicket.more.link.service.DefaultLinkParameterConversionService;
import org.iglooproject.wicket.more.link.service.ILinkParameterConversionService;
import org.iglooproject.wicket.more.notification.service.IHtmlNotificationCssService;
import org.iglooproject.wicket.more.notification.service.IWicketContextProvider;
import org.iglooproject.wicket.more.notification.service.PhlocCssHtmlNotificationCssServiceImpl;
import org.iglooproject.wicket.more.notification.service.WicketContextProviderImpl;
import org.iglooproject.wicket.more.rendering.service.RendererServiceImpl;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;

import test.wicket.more.business.WicketMoreTestBusinessPackage;

/**
 * Stub.
 */
@Configuration
@Import({
	JpaOnlyTestConfiguration.class,
	WicketMoreServiceConfig.class,
	WicketMoreApplicationPropertyRegistryConfig.class,
	SpringPropertyRegistryAutoConfiguration.class
})
@ComponentScan(basePackageClasses = WicketMoreTestBusinessPackage.class)
@EntityScan(basePackageClasses = WicketMoreTestBusinessPackage.class)
public class TestCommonConfiguration {
	
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
	public IRendererService rendererService(IWicketContextProvider wicketContextProvider) {
		return new RendererServiceImpl(wicketContextProvider);
	}
	
	@Bean
	public ILinkParameterConversionService linkParameterConversionService() {
		return new DefaultLinkParameterConversionService();
	}
	
	@Bean
	public IHtmlNotificationCssService htmlNotificationCssService() throws ServiceException {
		return new PhlocCssHtmlNotificationCssServiceImpl();
	}

}
