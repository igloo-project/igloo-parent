package fr.openwide.core.basicapp.init.config.spring;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import fr.openwide.core.basicapp.core.config.spring.BasicApplicationCoreCommonConfig;
import fr.openwide.core.basicapp.init.BasicApplicationInitPackage;

@Configuration
@Import({
	BasicApplicationCoreCommonConfig.class
})
@ComponentScan(
		basePackageClasses = {
			BasicApplicationInitPackage.class
		}
)
public class BasicApplicationInitConfig {
}
