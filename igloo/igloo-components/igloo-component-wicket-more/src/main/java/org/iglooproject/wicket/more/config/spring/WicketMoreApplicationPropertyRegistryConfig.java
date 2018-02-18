package org.iglooproject.wicket.more.config.spring;

import static org.iglooproject.wicket.more.property.WicketMorePropertyIds.AUTOCOMPLETE_LIMIT;
import static org.iglooproject.wicket.more.property.WicketMorePropertyIds.CONSOLE_GLOBAL_FEEDBACK_AUTOHIDE_DELAY_UNIT;
import static org.iglooproject.wicket.more.property.WicketMorePropertyIds.CONSOLE_GLOBAL_FEEDBACK_AUTOHIDE_DELAY_VALUE;
import static org.iglooproject.wicket.more.property.WicketMorePropertyIds.GLOBAL_FEEDBACK_AUTOHIDE_DELAY_UNIT;
import static org.iglooproject.wicket.more.property.WicketMorePropertyIds.GLOBAL_FEEDBACK_AUTOHIDE_DELAY_VALUE;
import static org.iglooproject.wicket.more.property.WicketMorePropertyIds.WICKET_DEFAULT_BACKGROUND_THREAD_CONTEXT_BUILDER_URL_SCHEME;
import static org.iglooproject.wicket.more.property.WicketMorePropertyIds.WICKET_DEFAULT_BACKGROUND_THREAD_CONTEXT_BUILDER_URL_SERVER_NAME;
import static org.iglooproject.wicket.more.property.WicketMorePropertyIds.WICKET_DEFAULT_BACKGROUND_THREAD_CONTEXT_BUILDER_URL_SERVER_PORT;
import static org.iglooproject.wicket.more.property.WicketMorePropertyIds.WICKET_DEFAULT_BACKGROUND_THREAD_CONTEXT_BUILDER_URL_SERVLET_PATH;
import static org.iglooproject.wicket.more.property.WicketMorePropertyIds.WICKET_APPLICATION_BACKGROUND_THREAD_CONTEXT_BUILDER_URL_SCHEME_TEMPLATE;
import static org.iglooproject.wicket.more.property.WicketMorePropertyIds.WICKET_APPLICATION_BACKGROUND_THREAD_CONTEXT_BUILDER_URL_SERVER_NAME_TEMPLATE;
import static org.iglooproject.wicket.more.property.WicketMorePropertyIds.WICKET_APPLICATION_BACKGROUND_THREAD_CONTEXT_BUILDER_URL_SERVER_PORT_TEMPLATE;
import static org.iglooproject.wicket.more.property.WicketMorePropertyIds.WICKET_APPLICATION_BACKGROUND_THREAD_CONTEXT_BUILDER_URL_SERVLET_PATH_TEMPLATE;
import static org.iglooproject.wicket.more.property.WicketMorePropertyIds.WICKET_DISK_DATA_STORE_IN_MEMORY_CACHE_SIZE;
import static org.iglooproject.wicket.more.property.WicketMorePropertyIds.WICKET_DISK_DATA_STORE_MAX_SIZE_PER_SESSION;
import static org.iglooproject.wicket.more.property.WicketMorePropertyIds.WICKET_DISK_DATA_STORE_PATH;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import com.google.common.base.Converter;
import com.google.common.base.Supplier;
import com.google.common.primitives.Ints;

import org.iglooproject.spring.config.spring.AbstractApplicationPropertyRegistryConfig;
import org.iglooproject.spring.property.service.IPropertyRegistry;
import org.iglooproject.spring.property.service.IPropertyService;

@Configuration
public class WicketMoreApplicationPropertyRegistryConfig extends AbstractApplicationPropertyRegistryConfig {
	
	@Autowired
	@Lazy
	private IPropertyService propertyService;

	@Override
	public void register(IPropertyRegistry registry) {
		registry.registerString(WICKET_DEFAULT_BACKGROUND_THREAD_CONTEXT_BUILDER_URL_SCHEME, "http");
		registry.registerString(WICKET_DEFAULT_BACKGROUND_THREAD_CONTEXT_BUILDER_URL_SERVER_NAME, "localhost");
		registry.registerInteger(WICKET_DEFAULT_BACKGROUND_THREAD_CONTEXT_BUILDER_URL_SERVER_PORT, 8080);
		registry.registerString(WICKET_DEFAULT_BACKGROUND_THREAD_CONTEXT_BUILDER_URL_SERVLET_PATH, "/");
		
		registry.register(
				WICKET_APPLICATION_BACKGROUND_THREAD_CONTEXT_BUILDER_URL_SCHEME_TEMPLATE,
				Converter.<String>identity(),
				new Supplier<String>() {
					@Override
					public String get() {
						return propertyService.get(WICKET_DEFAULT_BACKGROUND_THREAD_CONTEXT_BUILDER_URL_SCHEME);
					}
				});
		registry.register(
				WICKET_APPLICATION_BACKGROUND_THREAD_CONTEXT_BUILDER_URL_SERVER_NAME_TEMPLATE,
				Converter.<String>identity(),
				new Supplier<String>() {
					@Override
					public String get() {
						return propertyService.get(WICKET_DEFAULT_BACKGROUND_THREAD_CONTEXT_BUILDER_URL_SERVER_NAME);
					}
				});
		registry.register(
				WICKET_APPLICATION_BACKGROUND_THREAD_CONTEXT_BUILDER_URL_SERVER_PORT_TEMPLATE,
				Ints.stringConverter(),
				new Supplier<Integer>() {
					@Override
					public Integer get() {
						return propertyService.get(WICKET_DEFAULT_BACKGROUND_THREAD_CONTEXT_BUILDER_URL_SERVER_PORT);
					}
				});
		registry.register(
				WICKET_APPLICATION_BACKGROUND_THREAD_CONTEXT_BUILDER_URL_SERVLET_PATH_TEMPLATE,
				Converter.<String>identity(),
				new Supplier<String>() {
					@Override
					public String get() {
						return propertyService.get(WICKET_DEFAULT_BACKGROUND_THREAD_CONTEXT_BUILDER_URL_SERVLET_PATH);
					}
				});
		
		registry.registerString(WICKET_DISK_DATA_STORE_PATH, "");
		
		// Default to 0, see http://markmail.org/message/lq4lkfxi5whb5clr#query:+page:1+mid:m5qzptq24kxvmefo+state:results
		registry.registerInteger(WICKET_DISK_DATA_STORE_IN_MEMORY_CACHE_SIZE, 0);
		
		registry.registerInteger(WICKET_DISK_DATA_STORE_MAX_SIZE_PER_SESSION, 10);
		
		registry.registerInteger(GLOBAL_FEEDBACK_AUTOHIDE_DELAY_VALUE, 5);
		registry.registerEnum(GLOBAL_FEEDBACK_AUTOHIDE_DELAY_UNIT, TimeUnit.class, TimeUnit.SECONDS);
		registry.registerInteger(CONSOLE_GLOBAL_FEEDBACK_AUTOHIDE_DELAY_VALUE, 5);
		registry.registerEnum(CONSOLE_GLOBAL_FEEDBACK_AUTOHIDE_DELAY_UNIT, TimeUnit.class, TimeUnit.SECONDS);
		
		registry.registerInteger(AUTOCOMPLETE_LIMIT, 20);
	}

}
