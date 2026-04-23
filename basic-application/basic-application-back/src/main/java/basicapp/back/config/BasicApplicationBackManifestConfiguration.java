package basicapp.back.config;

import org.iglooproject.config.bootstrap.spring.annotations.ManifestPropertySource;
import org.springframework.context.annotation.Configuration;

@Configuration
@ManifestPropertySource(prefix = "basic-application.back")
public class BasicApplicationBackManifestConfiguration {}
