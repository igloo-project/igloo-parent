package test.wicket.more.link.descriptor.config.spring;

import org.apache.wicket.protocol.http.WebApplication;
import org.iglooproject.wicket.more.config.spring.AbstractWebappConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import test.wicket.more.link.descriptor.application.WicketMoreTestLinkDescriptorApplication;

/**
 * Stub.
 */
@Configuration
@Import({})
public class WicketMoreTestLinkDescriptorWebappConfig extends AbstractWebappConfig {

	@Override
	@Bean
	public WebApplication application() {
		return new WicketMoreTestLinkDescriptorApplication();
	}

}
