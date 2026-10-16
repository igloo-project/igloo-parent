package basicapp.front.config;

import igloo.julhelper.servlet.JakartaJulLoggingListener;
import igloo.log4j2jmx.servlet.JakartaLog4j2LoggingManagerListener;
import jakarta.servlet.ServletContext;
import jakarta.servlet.SessionTrackingMode;
import java.nio.charset.StandardCharsets;
import java.util.Set;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BasicApplicationFrontServletConfiguration implements ServletContextInitializer {

  @Override
  public void onStartup(ServletContext servletContext) {
    servletContext.setResponseCharacterEncoding(StandardCharsets.UTF_8.displayName());
    servletContext.setRequestCharacterEncoding(StandardCharsets.UTF_8.displayName());
    servletContext.setSessionTimeout(480);
    servletContext.setSessionTrackingModes(Set.of(SessionTrackingMode.COOKIE));
    servletContext.addListener(JakartaJulLoggingListener.class);
    servletContext.addListener(JakartaLog4j2LoggingManagerListener.class);
  }
}
