package igloo.jwt.spring;

import igloo.jwt.exception.ConvertTokenException;
import igloo.jwt.exception.InvalidTokenException;
import igloo.jwt.service.IJwtToPrincipalConverter;
import igloo.jwt.service.IJwtTokenService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import jakarta.servlet.http.HttpServletRequest;
import java.net.http.HttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
import org.springframework.security.web.context.NullSecurityContextRepository;

/**
 * {@link AbstractPreAuthenticatedProcessingFilter} that validate a JWT token and build a {@code P}
 * principal object. The way token is extracted from {@link HttpRequest} is performed by abstract
 * method {@link #extractToken(HttpServletRequest)}.
 */
public abstract class JwtPreAuthenticatedProcessingFilter<P>
    extends AbstractPreAuthenticatedProcessingFilter {

  private static final Logger LOGGER =
      LoggerFactory.getLogger(JwtPreAuthenticatedProcessingFilter.class);

  private final IJwtTokenService<P> jwtTokenService;

  private final IJwtToPrincipalConverter<P> jwtToPrincipalConverter;

  public JwtPreAuthenticatedProcessingFilter(
      AuthenticationManager authenticationManager,
      IJwtTokenService<P> tokenSecurityService,
      IJwtToPrincipalConverter<P> jwtToPrincipalConverter) {
    super();
    this.setAuthenticationManager(authenticationManager);
    this.jwtTokenService = tokenSecurityService;
    this.jwtToPrincipalConverter = jwtToPrincipalConverter;
    // prevent session creation
    // https://stackoverflow.com/questions/76955160/session-is-created-even-if-sessioncreationpolicy-stateless-is-set
    setSecurityContextRepository(new NullSecurityContextRepository());
  }

  @Override
  protected P getPreAuthenticatedPrincipal(HttpServletRequest request) {
    String token = extractToken(request);
    if (token == null) {
      return null;
    }

    // Token mapping
    try {
      Jws<Claims> claims = jwtTokenService.verifyToken(token);
      return jwtToPrincipalConverter.convert(claims);
    } catch (ExpiredJwtException | InvalidTokenException | ConvertTokenException e) {
      LOGGER.info("Invalid or expired token.", e);
    } catch (RuntimeException e) {
      if (LOGGER.isDebugEnabled()) {
        LOGGER.debug("Unexpected rrror extracting authentication from request", e);
      }
    }

    return null;
  }

  protected abstract String extractToken(HttpServletRequest request);

  @Override
  protected Object getPreAuthenticatedCredentials(HttpServletRequest request) {
    return "* NO PASSWORD *";
  }
}
