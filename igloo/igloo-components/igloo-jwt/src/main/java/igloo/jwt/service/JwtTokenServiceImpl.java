package igloo.jwt.service;

import igloo.jwt.exception.ExpiredTokenException;
import igloo.jwt.exception.InvalidTokenException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;
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
import org.springframework.security.oauth2.jwt.MappedJwtClaimSetConverter;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;

public class JwtTokenServiceImpl<P> implements IJwtTokenService<P> {

  public static final Logger LOGGER = LoggerFactory.getLogger(JwtTokenServiceImpl.class);

  /** Customize JWT token generation. */
  private final BiConsumer<P, JwtClaimsSet.Builder> jwtTokenBuilderCustomizer;

  /** Customize JWT header generation. */
  private final BiConsumer<P, JwsHeader.Builder> jwtTokenHeaderCustomizer;

  /** Encoder for token creation. May be null if only verification is needed. */
  private final JwtEncoder jwtEncoder;

  /** Decoder for token verification. */
  private final NimbusJwtDecoder jwtDecoder;

  private JwtTokenServiceImpl(
      BiConsumer<P, JwtClaimsSet.Builder> jwtTokenBuilderCustomizer,
      BiConsumer<P, JwsHeader.Builder> jwtTokenHeaderCustomizer,
      JwtEncoder jwtEncoder,
      NimbusJwtDecoder jwtDecoder) {
    this.jwtTokenBuilderCustomizer = jwtTokenBuilderCustomizer;
    this.jwtTokenHeaderCustomizer = jwtTokenHeaderCustomizer;
    this.jwtEncoder = jwtEncoder;
    this.jwtDecoder = jwtDecoder;
  }

  public static <P> Builder<P> builder(RSAPublicKey publicKey, RSAPrivateKey privateKey) {
    return new Builder<>(publicKey, privateKey);
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
    return generateJwt(principal, builderConsumer);
  }

  private String generateJwt(P principal, Consumer<JwtClaimsSet.Builder> builderCustomizer) {
    Instant now = Instant.now();
    Instant expiration = now.plus(Duration.ofMinutes(5));

    JwtClaimsSet.Builder claimsBuilder = JwtClaimsSet.builder().issuedAt(now).expiresAt(expiration);
    JwsHeader.Builder headerBuilder = JwsHeader.with(SignatureAlgorithm.RS256);

    // apply general consumer
    jwtTokenBuilderCustomizer.accept(principal, claimsBuilder);
    // apply adhoc consumer
    builderCustomizer.accept(claimsBuilder);

    jwtTokenHeaderCustomizer.accept(principal, headerBuilder);

    return jwtEncoder
        .encode(JwtEncoderParameters.from(headerBuilder.build(), claimsBuilder.build()))
        .getTokenValue();
  }

  @Override
  public Jwt verifyToken(String jwt) throws InvalidTokenException {
    try {
      // first: verify token (signature + expiration)
      return jwtDecoder.decode(jwt);
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

  public static class Builder<P> {
    private BiConsumer<P, JwtClaimsSet.Builder> jwtTokenBuilderCustomizer = (p, b) -> {};
    private BiConsumer<P, JwsHeader.Builder> jwtTokenHeaderCustomizer = (p, b) -> {};
    private final JwtEncoder jwtEncoder;
    private final NimbusJwtDecoder jwtDecoder;

    private Builder(RSAPublicKey publicKey, RSAPrivateKey privateKey) {
      if (publicKey == null) {
        throw new IllegalStateException("publicKey is required");
      }
      jwtEncoder =
          privateKey != null ? NimbusJwtEncoder.withKeyPair(publicKey, privateKey).build() : null;
      jwtDecoder =
          NimbusJwtDecoder.withPublicKey(publicKey)
              .signatureAlgorithm(SignatureAlgorithm.RS256)
              .build();
    }

    public Builder<P> jwtTokenBuilderCustomizer(
        BiConsumer<P, JwtClaimsSet.Builder> jwtTokenBuilderCustomizer) {
      this.jwtTokenBuilderCustomizer = jwtTokenBuilderCustomizer;
      return this;
    }

    public Builder<P> jwtTokenHeaderCustomizer(
        BiConsumer<P, JwsHeader.Builder> jwtTokenHeaderCustomizer) {
      this.jwtTokenHeaderCustomizer = jwtTokenHeaderCustomizer;
      return this;
    }

    public Builder<P> jwtValidator(List<OAuth2TokenValidator<Jwt>> jwtValidator) {
      jwtDecoder.setJwtValidator(JwtValidators.createDefaultWithValidators(jwtValidator));
      return this;
    }

    public Builder<P> claimTypeConverters(Map<String, Converter<Object, ?>> claimTypeConverters) {
      jwtDecoder.setClaimSetConverter(MappedJwtClaimSetConverter.withDefaults(claimTypeConverters));
      return this;
    }

    public JwtTokenServiceImpl<P> build() {
      return new JwtTokenServiceImpl<>(
          jwtTokenBuilderCustomizer, jwtTokenHeaderCustomizer, jwtEncoder, jwtDecoder);
    }
  }
}
