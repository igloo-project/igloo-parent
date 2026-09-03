package igloo.jwt.service;

import igloo.jwt.exception.ExpiredTokenException;
import igloo.jwt.exception.InvalidTokenException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.JwtValidationException;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.util.CollectionUtils;

public class JwtTokenServiceImpl<P> implements IJwtTokenService<P> {

  public static final Logger LOGGER = LoggerFactory.getLogger(JwtTokenServiceImpl.class);

  /** Customize JWT token generation. */
  private final BiConsumer<P, JwtClaimsSet.Builder> jwtTokenBuilderConsumer;

  /** Called for each generated token. */
  private final BiConsumer<P, String> jwtCreationCallback;

  /**
   * Called after each token validation. Must throw a {@link RuntimeException} if token validation
   * must be cancelled.
   */
  private final Consumer<String> jwtValidationCallback;

  /** Encoder for token creation. May be null if only verification is needed. */
  private final JwtEncoder jwtEncoder;

  /** Decoder for token verification. */
  private final NimbusJwtDecoder jwtDecoder;

  public JwtTokenServiceImpl(
      BiConsumer<P, JwtClaimsSet.Builder> jwtTokenBuilderConsumer,
      List<OAuth2TokenValidator<Jwt>> jwtValidator,
      BiConsumer<P, String> jwtCreationCallback,
      Consumer<String> jwtValidationCallback,
      RSAPublicKey publicKey,
      RSAPrivateKey privateKey) {
    this.jwtTokenBuilderConsumer = jwtTokenBuilderConsumer;
    this.jwtCreationCallback = jwtCreationCallback;
    this.jwtValidationCallback = jwtValidationCallback;
    this.jwtEncoder =
        privateKey != null ? NimbusJwtEncoder.withKeyPair(publicKey, privateKey).build() : null;
    this.jwtDecoder =
        NimbusJwtDecoder.withPublicKey(publicKey)
            .signatureAlgorithm(SignatureAlgorithm.RS256)
            .build();
    if (!CollectionUtils.isEmpty(jwtValidator)) {
      this.jwtDecoder.setJwtValidator(JwtValidators.createDefaultWithValidators(jwtValidator));
    }
  }

  @Override
  public String issueToken(P principal) {
    return issueToken(principal, x -> {});
  }

  @Override
  public String issueToken(P principal, Consumer<JwtClaimsSet.Builder> builderConsumer) {
    if (jwtEncoder == null) {
      throw new IllegalStateException("Cannot issue tokens: no private key configured.");
    }
    String jwt = generateJwt(principal, builderConsumer);
    jwtCreationCallback.accept(principal, jwt);
    return jwt;
  }

  private String generateJwt(P principal, Consumer<JwtClaimsSet.Builder> builderConsumer) {
    Instant now = Instant.now();
    Instant expiration = now.plus(Duration.ofMinutes(5));

    JwtClaimsSet.Builder claimsBuilder = JwtClaimsSet.builder().issuedAt(now).expiresAt(expiration);

    // apply general consumer
    jwtTokenBuilderConsumer.accept(principal, claimsBuilder);
    // apply adhoc consumer
    builderConsumer.accept(claimsBuilder);

    // TODO RFO, voir si on a besoin de modifier ça via consumer
    // Add a random attribute to get rid of any duplicates
    JwsHeader jwsHeader =
        JwsHeader.with(SignatureAlgorithm.RS256)
            .header("guid", UUID.randomUUID().toString())
            .build();

    return jwtEncoder
        .encode(JwtEncoderParameters.from(jwsHeader, claimsBuilder.build()))
        .getTokenValue();
  }

  @Override
  public Jwt verifyToken(String jwt) throws InvalidTokenException {
    try {
      // first: verify token (signature + expiration)
      Jwt token = jwtDecoder.decode(jwt);

      // second: invoke application verification; may perform nothing
      jwtValidationCallback.accept(jwt);

      return token;
    } catch (JwtValidationException e) {
      boolean expired =
          e.getErrors().stream()
              .anyMatch(
                  error ->
                      error.getDescription() != null && error.getDescription().contains("expired"));
      if (expired) {
        LOGGER.info("Token is expired.");
        throw new ExpiredTokenException("Token is expired.", e);
      }
      if (LOGGER.isDebugEnabled()) {
        LOGGER.debug("JWT validation exception.", e);
      }
      throw new InvalidTokenException("JWT validation failed.", e);
    } catch (JwtException e) {
      // covers BadJwtException and other JwtException subtypes
      if (LOGGER.isDebugEnabled()) {
        LOGGER.debug("JWT validation exception.", e);
      }
      throw new InvalidTokenException("JWT validation failed.", e);
    } catch (RuntimeException e) {
      // all unexpected exceptions wrapped in InvalidTokenException
      if (LOGGER.isDebugEnabled()) {
        LOGGER.debug("JWT parsing exception.", e);
      }
      throw new InvalidTokenException("Unexpected exception during JWT validation.", e);
    }
  }
}
