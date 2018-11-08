package org.iglooproject.test.wicket.more.config.spring;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(WicketMoreTestApplicationPropertyConfig.class)
public class WicketMoreTestCommonConfig {

}
