package test.web.config.spring;

import basicapp.front.config.spring.BasicApplicationWebappConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({
  BasicApplicationWebappConfig.class,
})
public class BasicApplicationWebappTestCommonConfig {}
