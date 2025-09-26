package igloo.jwt.spring;

import igloo.jwt.service.IJwtToPrincipalConverter;
import igloo.jwt.service.IJwtTokenService;
import jakarta.servlet.http.HttpServletRequest;
import org.iglooproject.spring.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;

/**
 * A {@link JwtPreAuthenticatedProcessingFilter} that extracts JWT token from bearer field.
 *
 * @param <P>
 */
public class JwtBearerPreAuthenticatedProcessingFilter<P>
    extends JwtPreAuthenticatedProcessingFilter<P> {

  private static final Logger LOGGER =
      LoggerFactory.getLogger(JwtBearerPreAuthenticatedProcessingFilter.class);

  private static final String BEARER_PREFIX = "Bearer ";

  public JwtBearerPreAuthenticatedProcessingFilter(
      AuthenticationManager authenticationManager,
      IJwtTokenService<P> tokenSecurityService,
      IJwtToPrincipalConverter<P> jwtToPrincipalConverter) {
    super(authenticationManager, tokenSecurityService, jwtToPrincipalConverter);
  }

  @Override
  protected String extractToken(HttpServletRequest request) {
    String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

    // Missing header
    if (!StringUtils.hasText(authorizationHeader)) {
      return null;
    }

    // Bad header format
    if (!authorizationHeader.startsWith(BEARER_PREFIX)) {
      if (LOGGER.isDebugEnabled()) {
        LOGGER.debug("Bad header format: {}...", authorizationHeader.substring(0, 10));
      }
      return null;
    }

    // Token extraction
    String token = null;
    try {
      token = extractToken(authorizationHeader);
    } catch (Exception e) {
      LOGGER.warn("JWT token cannot be extracted.", e);
      return null;
    }
    return token;
  }

  /** Extract token from header "Authorization: Bearer [token]" */
  private static String extractToken(String authorizationHeader) {
    if (!authorizationHeader.startsWith(BEARER_PREFIX)) {
      throw new IllegalArgumentException(
          "Unexpected bearer prefix (%s...).".formatted(authorizationHeader.substring(0, 10)));
    }
    return authorizationHeader
        .substring(BEARER_PREFIX.length(), authorizationHeader.length())
        .trim();
  }
}
