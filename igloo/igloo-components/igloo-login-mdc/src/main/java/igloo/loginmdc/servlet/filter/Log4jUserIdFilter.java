package igloo.loginmdc.servlet.filter;

import java.io.IOException;
import java.util.Optional;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import org.iglooproject.jpa.security.business.user.model.IUser;
import org.iglooproject.jpa.security.business.user.service.ISecurityUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

public class Log4jUserIdFilter implements Filter {

  private static final String LOG4J_USER_ID_FILTER_MAP_KEY = "user-id";

  public static final Logger LOGGER = LoggerFactory.getLogger(Log4jUserIdFilter.class);

  @Autowired private ISecurityUserService<? extends IUser> securityService;

  @Override
  public void init(FilterConfig filterConfig) {
    SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {
    try {
      if (request instanceof HttpServletRequest) {
        HttpServletRequest requestFacade = (HttpServletRequest) request;
        Optional.ofNullable(requestFacade.getSession(false))
            .map(
                session ->
                    session.getAttribute(
                        HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY))
            .map(o -> o instanceof SecurityContext ? (SecurityContext) o : null)
            .map(SecurityContext::getAuthentication)
            .map(Authentication::getName)
            .map(securityService::getByUsername)
            .map(IUser::getId)
            .ifPresent(id -> MDC.put(LOG4J_USER_ID_FILTER_MAP_KEY, id.toString()));
      }
    } catch (Exception e) {
      LOGGER.error("error during MDC 'user-id' creation", e);
    }

    try {
      chain.doFilter(request, response);
    } finally {
      MDC.remove(LOG4J_USER_ID_FILTER_MAP_KEY);
    }
  }
}
