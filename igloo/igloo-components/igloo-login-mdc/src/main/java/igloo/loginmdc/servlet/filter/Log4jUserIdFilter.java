package igloo.loginmdc.servlet.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Optional;
import org.iglooproject.jpa.security.business.user.model.IUser;
import org.iglooproject.jpa.security.business.user.service.ICoreUserSecurityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;

public class Log4jUserIdFilter implements Filter {

  private static final String LOG4J_USER_ID_FILTER_MAP_KEY = "user-id";

  public static final Logger LOGGER = LoggerFactory.getLogger(Log4jUserIdFilter.class);

  private final ICoreUserSecurityService<? extends IUser> securityService;

  public Log4jUserIdFilter(ICoreUserSecurityService<? extends IUser> securityService) {
    this.securityService = securityService;
  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {
    try {
      if (request instanceof HttpServletRequest requestFacade) {
        Optional.ofNullable(requestFacade.getSession(false))
            .map(
                session ->
                    session.getAttribute(
                        HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY))
            .map(o -> o instanceof SecurityContext securityContext ? securityContext : null)
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
