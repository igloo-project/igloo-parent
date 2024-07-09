package test.web.config.spring;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import basicapp.front.config.spring.BasicApplicationWebappConfig;

@Configuration
@Import({
	BasicApplicationWebappConfig.class,
})
public class BasicApplicationWebappTestCommonConfig {

}
