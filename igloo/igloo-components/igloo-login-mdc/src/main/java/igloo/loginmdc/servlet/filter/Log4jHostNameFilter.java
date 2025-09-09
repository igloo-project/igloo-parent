package igloo.loginmdc.servlet.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import java.io.IOException;
import org.slf4j.MDC;

public class Log4jHostNameFilter implements Filter {

  private static final String LOG4J_HOST_NAME_FILTER_MAP_KEY = "host-name";

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {
    MDC.put(LOG4J_HOST_NAME_FILTER_MAP_KEY, request.getServerName());
    try {
      chain.doFilter(request, response);
    } finally {
      MDC.remove(LOG4J_HOST_NAME_FILTER_MAP_KEY);
    }
  }
}
