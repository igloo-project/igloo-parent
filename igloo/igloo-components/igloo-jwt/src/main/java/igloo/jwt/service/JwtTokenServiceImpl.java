package igloo.jwt.service;

import igloo.jwt.exception.InvalidTokenException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParserBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Locator;
import java.security.Key;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JwtTokenServiceImpl<P> implements IJwtTokenService<P> {

  public static final Logger LOGGER = LoggerFactory.getLogger(JwtTokenServiceImpl.class);

  /** Customize JWT token generation. */
  private final BiConsumer<P, JwtBuilder> jwtTokenBuilderConsumer;

  /** Customize JWT token validation. */
  private final Consumer<JwtParserBuilder> jwtParserBuilderConsumer;

  /** Called for each generated token. */
  private final BiConsumer<P, String> jwtCreationCallback;

  /**
   * Called after each token validation. Must throw a {@link RuntimeException} if token validation
   * must be cancelled. Reusing an appropriate subclass of {@link JwtException} is preferred.
   */
  private final Consumer<String> jwtValidationCallback;

  /** Key for token signature. May be overridden by {@link #jwtTokenBuilderConsumer}. */
  private final Key signatureKey;

  /** Key locator for token verification. May be overridden by {@link #jwtParserBuilderConsumer}. */
  private final Locator<Key> keyLocator;

  public JwtTokenServiceImpl(
      BiConsumer<P, JwtBuilder> jwtTokenBuilderConsumer,
      Consumer<JwtParserBuilder> jwtParserBuilderConsumer,
      BiConsumer<P, String> jwtCreationCallback,
      Consumer<String> jwtValidationCallback,
      Key signatureKey,
      Locator<Key> keyLocator) {
    this.jwtTokenBuilderConsumer = jwtTokenBuilderConsumer;
    this.jwtParserBuilderConsumer = jwtParserBuilderConsumer;
    this.jwtCreationCallback = jwtCreationCallback;
    this.jwtValidationCallback = jwtValidationCallback;
    this.signatureKey = signatureKey;
    this.keyLocator = keyLocator;
  }

  @Override
  public String issueToken(P principal) {
    return issueToken(principal, x -> {});
  }

  @Override
  public String issueToken(P principal, Consumer<JwtBuilder> builderConsumer) {
    String jwt = generateJwt(principal, builderConsumer);
    jwtCreationCallback.accept(principal, jwt);
    return jwt;
  }

  private String generateJwt(P principal, Consumer<JwtBuilder> builderConsumer) {
    Instant now = Instant.now();
    Instant expiration = now.plus(Duration.ofMinutes(5));
    JwtBuilder builder =
        Jwts.builder()
            .issuedAt(new Date())
            .expiration(Date.from(expiration))
            // Add a random attribute to get rid of any duplicates
            .header()
            .add("guid", UUID.randomUUID().toString())
            .and()
            .signWith(signatureKey);
    // apply general consumer
    jwtTokenBuilderConsumer.accept(principal, builder);
    // apply adhoc consumer
    builderConsumer.accept(builder);
    return builder.compact();
  }

  @Override
  public Jws<Claims> verifyToken(String jwt) throws ExpiredJwtException, InvalidTokenException {
    try {
      // first: verify token
      JwtParserBuilder builder = Jwts.parser().keyLocator(keyLocator);
      jwtParserBuilderConsumer.accept(builder);
      Jws<Claims> claims = builder.build().parseSignedClaims(jwt);

      // second: invoke application verification; may perform nothing
      jwtValidationCallback.accept(jwt);

      return claims;
    } catch (ExpiredJwtException e) {
      // specifically handle expirations
      LOGGER.info("Token is expired.");
      throw e;
    } catch (JwtException e) {
      // allow debug level to track other exceptions
      // JwtException rethrown as-is
      if (LOGGER.isDebugEnabled()) {
        LOGGER.debug("JWT validation exception.", e);
      }
      throw e;
    } catch (RuntimeException e) {
      // allow debug level to track other exceptions
      // all exceptions wrapped in InvalidTokenException
      if (LOGGER.isDebugEnabled()) {
        LOGGER.debug("JWT parsing exception.", e);
      }
      throw new InvalidTokenException("Unexpected exception during JWT validation.", e);
    }
  }
}
