package org.iglooproject.test.rest.jersey2.config.spring;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(RestCommonTestConfiguration.class)
public class RestServerTestConfiguration {}
