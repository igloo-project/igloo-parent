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
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
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
        new JwtTokenServiceImpl<>(
            (principal, builder) -> builder.subject(principal),
            null,
            (principal, token) -> {},
            token -> {},
            publicKey,
            privateKey);

    String token = service.issueToken("testUser");
    Assertions.assertThat(token).isNotNull();

    Jwt jwt = service.verifyToken(token);
    Assertions.assertThat(jwt.getSubject()).isEqualTo("testUser");
    Assertions.assertThat(jwt.getIssuedAt()).isNotNull();
    Assertions.assertThat(jwt.getExpiresAt()).isNotNull();
    Assertions.assertThat(jwt.getExpiresAt()).isAfter(jwt.getIssuedAt());
  }

  /** Creation callback is called. */
  @Test
  void testCreationCallbackIsCalled() {
    List<String> callbackTokens = new ArrayList<>();
    List<String> callbackPrincipals = new ArrayList<>();

    JwtTokenServiceImpl<String> service =
        new JwtTokenServiceImpl<>(
            (principal, builder) -> builder.subject(principal),
            null,
            (principal, token) -> {
              callbackPrincipals.add(principal);
              callbackTokens.add(token);
            },
            token -> {},
            publicKey,
            privateKey);

    String token = service.issueToken("callbackUser");

    Assertions.assertThat(callbackTokens).hasSize(1);
    Assertions.assertThat(callbackTokens.get(0)).isEqualTo(token);
    Assertions.assertThat(callbackPrincipals).containsExactly("callbackUser");
  }

  /** Token customization via adhoc consumer. */
  @Test
  void testTokenCustomization() throws Exception {
    JwtTokenServiceImpl<String> service =
        new JwtTokenServiceImpl<>(
            (principal, builder) -> builder.subject(principal),
            null,
            (principal, token) -> {},
            token -> {},
            publicKey,
            privateKey);

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

  /** Custom validation callback is called. */
  @Test
  void testValidationCallbackIsCalled() throws Exception {
    AtomicBoolean callbackCalled = new AtomicBoolean(false);

    JwtTokenServiceImpl<String> service =
        new JwtTokenServiceImpl<>(
            (principal, builder) -> builder.subject(principal),
            null,
            (principal, token) -> {},
            token -> callbackCalled.set(true),
            publicKey,
            privateKey);

    String token = service.issueToken("validationUser");
    service.verifyToken(token);

    Assertions.assertThat(callbackCalled).isTrue();
  }

  /** Custom validation callback interrupts validation. */
  @Test
  void testValidationCallbackInterruptsValidation() {
    JwtTokenServiceImpl<String> service =
        new JwtTokenServiceImpl<>(
            (principal, builder) -> builder.subject(principal),
            null,
            (principal, token) -> {},
            token -> {
              throw new RuntimeException("Validation interrupted by callback");
            },
            publicKey,
            privateKey);

    String token = service.issueToken("interruptUser");

    Assertions.assertThatThrownBy(() -> service.verifyToken(token))
        .isInstanceOf(InvalidTokenException.class)
        .hasCauseInstanceOf(RuntimeException.class)
        .hasRootCauseMessage("Validation interrupted by callback");
  }

  /** Invalid token throws InvalidTokenException. */
  @Test
  void testInvalidToken() {
    JwtTokenServiceImpl<String> service =
        new JwtTokenServiceImpl<>(
            (principal, builder) -> builder.subject(principal),
            null,
            (principal, token) -> {},
            token -> {},
            publicKey,
            privateKey);

    Assertions.assertThatThrownBy(() -> service.verifyToken("invalid.token.value"))
        .isInstanceOf(InvalidTokenException.class);
  }

  /** Expired token throws InvalidTokenException. */
  @Test
  void testExpiredToken() {
    JwtTokenServiceImpl<String> service =
        new JwtTokenServiceImpl<>(
            (principal, builder) -> builder.subject(principal),
            null,
            (principal, token) -> {},
            token -> {},
            publicKey,
            privateKey);

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
        new JwtTokenServiceImpl<>(
            (principal, builder) -> builder.subject(principal),
            null,
            (principal, token) -> {},
            token -> {},
            otherPublicKey,
            otherPrivateKey);

    String token = otherService.issueToken("wrongKeyUser");

    // Verify with original key pair -> should fail
    JwtTokenServiceImpl<String> service =
        new JwtTokenServiceImpl<>(
            (principal, builder) -> builder.subject(principal),
            null,
            (principal, token2) -> {},
            token2 -> {},
            publicKey,
            privateKey);

    Assertions.assertThatThrownBy(() -> service.verifyToken(token))
        .isInstanceOf(InvalidTokenException.class);
  }

  /** Issuer validation accepts matching issuer. */
  @Test
  void testIssuerValidatorAccepts() throws Exception {
    JwtTokenServiceImpl<String> service =
        new JwtTokenServiceImpl<>(
            (principal, builder) -> builder.subject(principal).issuer("https://my-issuer"),
            List.of(new JwtIssuerValidator("https://my-issuer")),
            (principal, token) -> {},
            token -> {},
            publicKey,
            privateKey);

    String token = service.issueToken("issuerUser");
    Jwt jwt = service.verifyToken(token);
    Assertions.assertThat(jwt.getIssuer().toString()).isEqualTo("https://my-issuer");
  }

  /** Issuer validation rejects wrong issuer. */
  @Test
  void testIssuerValidatorRejects() {
    JwtTokenServiceImpl<String> issuingService =
        new JwtTokenServiceImpl<>(
            (principal, builder) -> builder.subject(principal).issuer("https://wrong-issuer"),
            null,
            (principal, token) -> {},
            token -> {},
            publicKey,
            privateKey);

    String token = issuingService.issueToken("issuerUser");

    JwtTokenServiceImpl<String> validatingService =
        new JwtTokenServiceImpl<>(
            (principal, builder) -> builder.subject(principal),
            List.of(new JwtIssuerValidator("https://expected-issuer")),
            (principal, token2) -> {},
            token2 -> {},
            publicKey,
            privateKey);

    Assertions.assertThatThrownBy(() -> validatingService.verifyToken(token))
        .isInstanceOf(InvalidTokenException.class);
  }

  /** Cannot issue tokens without private key. */
  @Test
  void testIssueWithoutPrivateKey() {
    JwtTokenServiceImpl<String> verifyOnlyService =
        new JwtTokenServiceImpl<>(
            (principal, builder) -> builder.subject(principal),
            null,
            (principal, token) -> {},
            token -> {},
            publicKey,
            null);

    Assertions.assertThatThrownBy(() -> verifyOnlyService.issueToken("user"))
        .isInstanceOf(IllegalStateException.class);
  }
}
