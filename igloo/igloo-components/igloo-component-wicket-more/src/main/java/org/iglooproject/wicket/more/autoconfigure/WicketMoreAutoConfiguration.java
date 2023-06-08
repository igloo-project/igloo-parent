package org.iglooproject.wicket.more.autoconfigure;

import static org.iglooproject.wicket.more.property.WicketMorePropertyIds.AUTOCOMPLETE_LIMIT;
import static org.iglooproject.wicket.more.property.WicketMorePropertyIds.AUTOPREFIXER_ENABLED;
import static org.iglooproject.wicket.more.property.WicketMorePropertyIds.CONSOLE_GLOBAL_FEEDBACK_AUTOHIDE_DELAY_UNIT;
import static org.iglooproject.wicket.more.property.WicketMorePropertyIds.CONSOLE_GLOBAL_FEEDBACK_AUTOHIDE_DELAY_VALUE;
import static org.iglooproject.wicket.more.property.WicketMorePropertyIds.GLOBAL_FEEDBACK_AUTOHIDE_DELAY_UNIT;
import static org.iglooproject.wicket.more.property.WicketMorePropertyIds.GLOBAL_FEEDBACK_AUTOHIDE_DELAY_VALUE;
import static org.iglooproject.wicket.more.property.WicketMorePropertyIds.SCSS_STATIC_ENABLED;
import static org.iglooproject.wicket.more.property.WicketMorePropertyIds.SCSS_STATIC_RESOURCE_PATH;
import static org.iglooproject.wicket.more.property.WicketMorePropertyIds.WICKET_APPLICATION_BACKGROUND_THREAD_CONTEXT_BUILDER_URL_SCHEME_TEMPLATE;
import static org.iglooproject.wicket.more.property.WicketMorePropertyIds.WICKET_APPLICATION_BACKGROUND_THREAD_CONTEXT_BUILDER_URL_SERVER_NAME_TEMPLATE;
import static org.iglooproject.wicket.more.property.WicketMorePropertyIds.WICKET_APPLICATION_BACKGROUND_THREAD_CONTEXT_BUILDER_URL_SERVER_PORT_TEMPLATE;
import static org.iglooproject.wicket.more.property.WicketMorePropertyIds.WICKET_APPLICATION_BACKGROUND_THREAD_CONTEXT_BUILDER_URL_SERVLET_PATH_TEMPLATE;
import static org.iglooproject.wicket.more.property.WicketMorePropertyIds.WICKET_DEFAULT_BACKGROUND_THREAD_CONTEXT_BUILDER_URL_SCHEME;
import static org.iglooproject.wicket.more.property.WicketMorePropertyIds.WICKET_DEFAULT_BACKGROUND_THREAD_CONTEXT_BUILDER_URL_SERVER_NAME;
import static org.iglooproject.wicket.more.property.WicketMorePropertyIds.WICKET_DEFAULT_BACKGROUND_THREAD_CONTEXT_BUILDER_URL_SERVER_PORT;
import static org.iglooproject.wicket.more.property.WicketMorePropertyIds.WICKET_DEFAULT_BACKGROUND_THREAD_CONTEXT_BUILDER_URL_SERVLET_PATH;
import static org.iglooproject.wicket.more.property.WicketMorePropertyIds.WICKET_DISK_DATA_STORE_MAX_SIZE_PER_SESSION;
import static org.iglooproject.wicket.more.property.WicketMorePropertyIds.WICKET_DISK_DATA_STORE_PATH;

import java.util.concurrent.TimeUnit;

import org.apache.wicket.Page;
import org.apache.wicket.protocol.http.WebApplication;
import org.iglooproject.functional.Functions2;
import org.iglooproject.functional.Supplier2;
import org.iglooproject.jpa.exception.ServiceException;
import org.iglooproject.jpa.more.rendering.service.IRendererService;
import org.iglooproject.sass.service.StaticResourceHelper;
import org.iglooproject.spring.config.spring.IPropertyRegistryConfig;
import org.iglooproject.spring.property.service.IPropertyRegistry;
import org.iglooproject.spring.property.service.IPropertyService;
import org.iglooproject.wicket.more.WicketMorePackage;
import org.iglooproject.wicket.more.link.service.DefaultLinkParameterConversionService;
import org.iglooproject.wicket.more.link.service.ILinkParameterConversionService;
import org.iglooproject.wicket.more.notification.service.IHtmlNotificationCssService;
import org.iglooproject.wicket.more.notification.service.IWicketContextProvider;
import org.iglooproject.wicket.more.notification.service.PhlocCssHtmlNotificationCssServiceImpl;
import org.iglooproject.wicket.more.notification.service.WicketContextProviderImpl;
import org.iglooproject.wicket.more.rendering.service.RendererServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.context.annotation.ComponentScan.Filter;

import com.google.common.base.Converter;
import com.google.common.primitives.Ints;

@Configuration
@ComponentScan(
		basePackageClasses = WicketMorePackage.class,
		excludeFilters = @Filter(Configuration.class)
)
@ConditionalOnClass(WebApplication.class)
public class WicketMoreAutoConfiguration implements IPropertyRegistryConfig {
	
	@Autowired
	@Lazy
	private IPropertyService propertyService;

	@Override
	public void register(IPropertyRegistry registry) {
		registry.registerBoolean(AUTOPREFIXER_ENABLED, true);
		registry.registerBoolean(SCSS_STATIC_ENABLED, true);
		registry.registerString(SCSS_STATIC_RESOURCE_PATH, StaticResourceHelper.DEFAULT_STATIC_SCSS_RESOURCE_PATH);
		
		registry.registerString(WICKET_DEFAULT_BACKGROUND_THREAD_CONTEXT_BUILDER_URL_SCHEME, "http");
		registry.registerString(WICKET_DEFAULT_BACKGROUND_THREAD_CONTEXT_BUILDER_URL_SERVER_NAME, "localhost");
		registry.registerInteger(WICKET_DEFAULT_BACKGROUND_THREAD_CONTEXT_BUILDER_URL_SERVER_PORT, 8080);
		registry.registerString(WICKET_DEFAULT_BACKGROUND_THREAD_CONTEXT_BUILDER_URL_SERVLET_PATH, "/");
		
		registry.register(
				WICKET_APPLICATION_BACKGROUND_THREAD_CONTEXT_BUILDER_URL_SCHEME_TEMPLATE,
				Functions2.from(Converter.<String>identity()),
				(Supplier2<? extends String>) () -> propertyService.get(WICKET_DEFAULT_BACKGROUND_THREAD_CONTEXT_BUILDER_URL_SCHEME)
		);
		registry.register(
				WICKET_APPLICATION_BACKGROUND_THREAD_CONTEXT_BUILDER_URL_SERVER_NAME_TEMPLATE,
				Functions2.from(Converter.<String>identity()),
				(Supplier2<? extends String>) () -> propertyService.get(WICKET_DEFAULT_BACKGROUND_THREAD_CONTEXT_BUILDER_URL_SERVER_NAME)
		);
		registry.register(
				WICKET_APPLICATION_BACKGROUND_THREAD_CONTEXT_BUILDER_URL_SERVER_PORT_TEMPLATE,
				Functions2.from(Ints.stringConverter()),
				(Supplier2<? extends Integer>) () -> propertyService.get(WICKET_DEFAULT_BACKGROUND_THREAD_CONTEXT_BUILDER_URL_SERVER_PORT)
		);
		registry.register(
				WICKET_APPLICATION_BACKGROUND_THREAD_CONTEXT_BUILDER_URL_SERVLET_PATH_TEMPLATE,
				Functions2.from(Converter.<String>identity()),
				(Supplier2<? extends String>) () -> propertyService.get(WICKET_DEFAULT_BACKGROUND_THREAD_CONTEXT_BUILDER_URL_SERVLET_PATH)
		);
		
		registry.registerString(WICKET_DISK_DATA_STORE_PATH, "");
		registry.registerInteger(WICKET_DISK_DATA_STORE_MAX_SIZE_PER_SESSION, 10);
		
		registry.registerInteger(GLOBAL_FEEDBACK_AUTOHIDE_DELAY_VALUE, 5);
		registry.registerEnum(GLOBAL_FEEDBACK_AUTOHIDE_DELAY_UNIT, TimeUnit.class, TimeUnit.SECONDS);
		registry.registerInteger(CONSOLE_GLOBAL_FEEDBACK_AUTOHIDE_DELAY_VALUE, 5);
		registry.registerEnum(CONSOLE_GLOBAL_FEEDBACK_AUTOHIDE_DELAY_UNIT, TimeUnit.class, TimeUnit.SECONDS);
		
		registry.registerInteger(AUTOCOMPLETE_LIMIT, 20);
	}

	// TODO igloo-boot ?
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
