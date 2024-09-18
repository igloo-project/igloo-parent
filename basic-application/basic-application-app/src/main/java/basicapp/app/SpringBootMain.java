package basicapp.app;

import basicapp.front.config.spring.BasicApplicationWebappConfig;
import org.iglooproject.config.bootstrap.spring.ExtendedApplicationContextInitializer;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(BasicApplicationWebappConfig.class)
public class SpringBootMain extends SpringBootServletInitializer {

  public static void main(String[] args) {
    doConfigure(new SpringApplicationBuilder()).run(args);
  }

  @Override
  protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
    return SpringBootMain.doConfigure(application);
  }

  /** Common configuration. */
  public static SpringApplicationBuilder doConfigure(SpringApplicationBuilder application) {
    return application
        .sources(BasicApplicationWebappConfig.class)
        .initializers(new ExtendedApplicationContextInitializer());
  }
}
