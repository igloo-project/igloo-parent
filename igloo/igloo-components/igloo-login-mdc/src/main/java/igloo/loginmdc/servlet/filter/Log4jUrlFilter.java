package igloo.loginmdc.servlet.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import org.slf4j.MDC;

public class Log4jUrlFilter implements Filter {

  private static final String LOG4J_URL_FILTER_MAP_KEY = "ow-url";

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {
    if (request instanceof HttpServletRequest httpRequest) {
      StringBuilder builder = new StringBuilder();
      builder.append(httpRequest.getRequestURI());
      if (httpRequest.getQueryString() != null) {
        builder.append("?");
        builder.append(httpRequest.getQueryString());
      }
      MDC.put(LOG4J_URL_FILTER_MAP_KEY, builder.toString());
    }
    try {
      chain.doFilter(request, response);
    } finally {
      MDC.remove(LOG4J_URL_FILTER_MAP_KEY);
    }
  }
}
