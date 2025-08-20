package basicapp.back.config.spring;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebAutoConfiguration;
import org.springframework.boot.autoconfigure.freemarker.FreeMarkerAutoConfiguration;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableAutoConfiguration(
    exclude = {
      FreeMarkerAutoConfiguration.class, // Not used and trigger a warning at startup
      // trigger warnings at startup
      // ... Is this bean getting eagerly injected into a currently created BeanPostProcessor
      // [projectingArgumentResolverBeanPostProcessor] ...
      // https://github.com/spring-projects/spring-data-jpa/issues/3244
      // https://github.com/spring-projects/spring-batch/issues/4489
      // https://github.com/spring-projects/spring-boot/issues/38558
      // https://github.com/spring-projects/spring-security/issues/14209
      // https://github.com/spring-projects/spring-ws/issues/1391
      SpringDataWebAutoConfiguration.class
    })
public class BasicApplicationBackBaseConfiguration {}
