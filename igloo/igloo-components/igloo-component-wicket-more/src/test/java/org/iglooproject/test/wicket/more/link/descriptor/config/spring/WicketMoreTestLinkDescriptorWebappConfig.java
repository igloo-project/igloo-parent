package org.iglooproject.test.wicket.more.link.descriptor.config.spring;

import org.apache.wicket.protocol.http.WebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import org.iglooproject.test.wicket.more.config.spring.WicketMoreTestCoreCommonConfig;
import org.iglooproject.test.wicket.more.link.descriptor.application.WicketMoreTestLinkDescriptorApplication;
import org.iglooproject.wicket.more.config.spring.AbstractWebappConfig;

/**
 * Stub.
 */
@Configuration
@Import({
	WicketMoreTestCoreCommonConfig.class
})
public class WicketMoreTestLinkDescriptorWebappConfig extends AbstractWebappConfig {

	@Override
	@Bean
	public WebApplication application() {
		return new WicketMoreTestLinkDescriptorApplication();
	}

}
