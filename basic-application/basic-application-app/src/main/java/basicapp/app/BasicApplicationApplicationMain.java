package basicapp.app;

import basicapp.app.config.BasicApplicationApplicationMainConfiguration;
import org.iglooproject.config.bootstrap.spring.ExtendedApplicationContextInitializer;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(BasicApplicationApplicationMainConfiguration.class)
public class BasicApplicationApplicationMain extends SpringBootServletInitializer {

  public static void main(String[] args) {
    doConfigure(new SpringApplicationBuilder()).run(args);
  }

  public static SpringApplicationBuilder doConfigure(SpringApplicationBuilder application) {
    return application
        .sources(BasicApplicationApplicationMain.class)
        .initializers(new ExtendedApplicationContextInitializer());
  }

  @Override
  protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
    return BasicApplicationApplicationMain.doConfigure(application);
  }
}
