package org.iglooproject.test.config.bootstrap.spring.util;

import org.iglooproject.config.bootstrap.spring.annotations.ManifestPropertySource;
import org.iglooproject.config.bootstrap.spring.config.ManifestPropertySourceConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ManifestPropertySource(prefix = "test")
@Import(ManifestPropertySourceConfiguration.class)
public class ManifestConfig extends AbstractBootstrapTestCase {}
