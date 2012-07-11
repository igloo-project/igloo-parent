package fr.openwide.core.basicapp.init.config.spring;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import fr.openwide.core.basicapp.core.config.spring.BasicApplicationCoreCommonConfig;

@Configuration
@Import({
	BasicApplicationCoreCommonConfig.class
})
public class BasicApplicationInitCoreConfig {

}
