package org.iglooproject.wicket.bootstrap5.config.spring;

import org.iglooproject.wicket.more.config.spring.AbstractWebappConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({
	WicketBootstrapServiceConfig.class,
})
public abstract class AbstractBootstrapWebappConfig extends AbstractWebappConfig {

}
