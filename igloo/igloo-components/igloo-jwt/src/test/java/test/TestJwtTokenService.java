package test;

import igloo.jwt.exception.ExpiredTokenException;
import igloo.jwt.exception.InvalidTokenException;
import igloo.jwt.service.JwtTokenServiceImpl;
import igloo.jwt.util.KeyLoader;
import java.io.InputStream;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtIssuerValidator;
import org.springframework.security.oauth2.jwt.JwtValidationException;

public class TestJwtTokenService {

  private static RSAPublicKey publicKey;
  private static RSAPrivateKey privateKey;

  @BeforeAll
  static void setUp() throws Exception {
    ClassLoader cl = Thread.currentThread().getContextClassLoader();
    try (InputStream priv = cl.getResourceAsStream("private_key.pem");
        InputStream pub = cl.getResourceAsStream("public_key.pem")) {
      privateKey = (RSAPrivateKey) KeyLoader.loadPrivateKey(priv);
      publicKey = (RSAPublicKey) KeyLoader.loadPublicKey(pub);
    }
  }

  /** Basic generation and validation. */
  @Test
  void testIssueAndVerify() throws Exception {
    JwtTokenServiceImpl<String> service =
        JwtTokenServiceImpl.<String>builder(publicKey, privateKey)
            .jwtTokenBuilderCustomizer((principal, builder) -> builder.subject(principal))
            .build();

    String token = service.issueToken("testUser");
    Assertions.assertThat(token).isNotNull();

    Jwt jwt = service.verifyToken(token);
    Assertions.assertThat(jwt.getSubject()).isEqualTo("testUser");
    Assertions.assertThat(jwt.getIssuedAt()).isNotNull();
    Assertions.assertThat(jwt.getExpiresAt()).isNotNull();
    Assertions.assertThat(jwt.getExpiresAt()).isAfter(jwt.getIssuedAt());
  }

  /** Token customization via adhoc consumer. */
  @Test
  void testTokenCustomization() throws Exception {
    JwtTokenServiceImpl<String> service =
        JwtTokenServiceImpl.<String>builder(publicKey, privateKey)
            .jwtTokenBuilderCustomizer((principal, builder) -> builder.subject(principal))
            .build();

    String token =
        service.issueToken(
            "customUser",
            builder ->
                builder
                    .issuer("https://test-issuer")
                    .audience(List.of("test-audience"))
                    .claim("customClaim", "customValue"));

    Jwt jwt = service.verifyToken(token);
    Assertions.assertThat(jwt.getIssuer().toString()).isEqualTo("https://test-issuer");
    Assertions.assertThat(jwt.getAudience()).containsExactly("test-audience");
    Assertions.assertThat(jwt.<String>getClaim("customClaim")).isEqualTo("customValue");
  }

  /** Invalid token throws InvalidTokenException. */
  @Test
  void testInvalidToken() {
    JwtTokenServiceImpl<String> service =
        JwtTokenServiceImpl.<String>builder(publicKey, privateKey)
            .jwtTokenBuilderCustomizer((principal, builder) -> builder.subject(principal))
            .build();

    Assertions.assertThatThrownBy(() -> service.verifyToken("invalid.token.value"))
        .isInstanceOf(InvalidTokenException.class);
  }

  /** Expired token throws InvalidTokenException. */
  @Test
  void testExpiredToken() {
    JwtTokenServiceImpl<String> service =
        JwtTokenServiceImpl.<String>builder(publicKey, privateKey)
            .jwtTokenBuilderCustomizer((principal, builder) -> builder.subject(principal))
            .build();

    // Issue a token that is already expired
    Instant past = Instant.now().minusSeconds(120);
    String token =
        service.issueToken(
            "expiredUser", builder -> builder.issuedAt(past).expiresAt(past.plusSeconds(1)));

    Assertions.assertThatThrownBy(() -> service.verifyToken(token))
        .isInstanceOf(ExpiredTokenException.class)
        .hasCauseInstanceOf(JwtValidationException.class);
  }

  /** Token signed with a different key fails validation. */
  @Test
  void testWrongSignatureKey() throws Exception {
    KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
    keyGen.initialize(2048);
    java.security.KeyPair otherKeyPair = keyGen.generateKeyPair();
    RSAPublicKey otherPublicKey = (RSAPublicKey) otherKeyPair.getPublic();
    RSAPrivateKey otherPrivateKey = (RSAPrivateKey) otherKeyPair.getPrivate();

    // Issue with other key pair
    JwtTokenServiceImpl<String> otherService =
        JwtTokenServiceImpl.<String>builder(otherPublicKey, otherPrivateKey)
            .jwtTokenBuilderCustomizer((principal, builder) -> builder.subject(principal))
            .build();

    String token = otherService.issueToken("wrongKeyUser");

    // Verify with original key pair -> should fail
    JwtTokenServiceImpl<String> service =
        JwtTokenServiceImpl.<String>builder(publicKey, privateKey)
            .jwtTokenBuilderCustomizer((principal, builder) -> builder.subject(principal))
            .build();

    Assertions.assertThatThrownBy(() -> service.verifyToken(token))
        .isInstanceOf(InvalidTokenException.class);
  }

  /** Issuer validation accepts matching issuer. */
  @Test
  void testIssuerValidatorAccepts() throws Exception {
    JwtTokenServiceImpl<String> service =
        JwtTokenServiceImpl.<String>builder(publicKey, privateKey)
            .jwtTokenBuilderCustomizer(
                (principal, builder) -> builder.subject(principal).issuer("https://my-issuer"))
            .jwtValidator(List.of(new JwtIssuerValidator("https://my-issuer")))
            .build();

    String token = service.issueToken("issuerUser");
    Jwt jwt = service.verifyToken(token);
    Assertions.assertThat(jwt.getIssuer().toString()).isEqualTo("https://my-issuer");
  }

  /** Issuer validation rejects wrong issuer. */
  @Test
  void testIssuerValidatorRejects() {
    JwtTokenServiceImpl<String> issuingService =
        JwtTokenServiceImpl.<String>builder(publicKey, privateKey)
            .jwtTokenBuilderCustomizer(
                (principal, builder) -> builder.subject(principal).issuer("https://wrong-issuer"))
            .build();

    String token = issuingService.issueToken("issuerUser");

    JwtTokenServiceImpl<String> validatingService =
        JwtTokenServiceImpl.<String>builder(publicKey, privateKey)
            .jwtTokenBuilderCustomizer((principal, builder) -> builder.subject(principal))
            .jwtValidator(List.of(new JwtIssuerValidator("https://expected-issuer")))
            .build();

    Assertions.assertThatThrownBy(() -> validatingService.verifyToken(token))
        .isInstanceOf(InvalidTokenException.class);
  }

  /** Cannot issue tokens without private key. */
  @Test
  void testIssueWithoutPrivateKey() {
    JwtTokenServiceImpl<String> verifyOnlyService =
        JwtTokenServiceImpl.<String>builder(publicKey, null)
            .jwtTokenBuilderCustomizer((principal, builder) -> builder.subject(principal))
            .build();

    Assertions.assertThatThrownBy(() -> verifyOnlyService.issueToken("user"))
        .isInstanceOf(IllegalStateException.class);
  }

  /** Header customizer adds custom headers to the JWT. */
  @Test
  void testHeaderCustomizer() throws Exception {
    JwtTokenServiceImpl<String> service =
        JwtTokenServiceImpl.<String>builder(publicKey, privateKey)
            .jwtTokenBuilderCustomizer((principal, builder) -> builder.subject(principal))
            .jwtTokenHeaderCustomizer(
                (principal, headerBuilder) ->
                    headerBuilder.header("x-custom-header", "customHeaderValue"))
            .build();

    String token = service.issueToken("headerUser");
    Jwt jwt = service.verifyToken(token);

    Assertions.assertThat(jwt.getHeaders()).containsEntry("x-custom-header", "customHeaderValue");
  }

  /** Claim type converter transforms a claim value during decoding. */
  @Test
  void testClaimTypeConverters() throws Exception {
    Converter<Object, Integer> stringToIntConverter = source -> Integer.valueOf(source.toString());

    JwtTokenServiceImpl<String> service =
        JwtTokenServiceImpl.<String>builder(publicKey, privateKey)
            .jwtTokenBuilderCustomizer(
                (principal, builder) -> builder.subject(principal).claim("numericValue", "42"))
            .claimTypeConverters(Map.of("numericValue", stringToIntConverter))
            .build();

    String token = service.issueToken("converterUser");
    Jwt jwt = service.verifyToken(token);

    Assertions.assertThat(jwt.<Integer>getClaim("numericValue")).isEqualTo(42);
  }

  /** Builder with null publicKey throws IllegalStateException. */
  @Test
  void testBuilderWithNullPublicKey() {
    Assertions.assertThatThrownBy(() -> JwtTokenServiceImpl.<String>builder(null, privateKey))
        .isInstanceOf(IllegalStateException.class);
  }

  /** Verify-only service (no private key) can verify tokens issued by another service. */
  @Test
  void testVerifyOnlyService() throws Exception {
    JwtTokenServiceImpl<String> issuingService =
        JwtTokenServiceImpl.<String>builder(publicKey, privateKey)
            .jwtTokenBuilderCustomizer((principal, builder) -> builder.subject(principal))
            .build();

    String token = issuingService.issueToken("verifyOnlyUser");

    JwtTokenServiceImpl<String> verifyOnlyService =
        JwtTokenServiceImpl.<String>builder(publicKey, null).build();

    Jwt jwt = verifyOnlyService.verifyToken(token);
    Assertions.assertThat(jwt.getSubject()).isEqualTo("verifyOnlyUser");
  }

  /** Verify with null token throws InvalidTokenException. */
  @Test
  void testVerifyNullToken() {
    JwtTokenServiceImpl<String> service =
        JwtTokenServiceImpl.<String>builder(publicKey, privateKey).build();

    Assertions.assertThatThrownBy(() -> service.verifyToken(null))
        .isInstanceOf(InvalidTokenException.class);
  }
}
