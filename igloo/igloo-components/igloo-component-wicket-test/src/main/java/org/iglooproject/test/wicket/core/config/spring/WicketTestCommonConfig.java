package org.iglooproject.test.wicket.core.config.spring;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(WicketTestApplicationPropertyConfig.class)
public class WicketTestCommonConfig {}
