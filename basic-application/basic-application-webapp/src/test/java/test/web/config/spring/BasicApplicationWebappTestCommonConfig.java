package test.web.config.spring;

import org.iglooproject.basicapp.web.application.config.spring.BasicApplicationWebappConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({
	BasicApplicationWebappConfig.class,
})
public class BasicApplicationWebappTestCommonConfig {

}
