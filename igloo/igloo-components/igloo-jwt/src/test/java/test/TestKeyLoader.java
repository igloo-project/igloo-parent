package test;

import igloo.jwt.util.KeyLoader;
import java.io.InputStream;
import java.security.PrivateKey;
import java.security.PublicKey;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestKeyLoader {

  @Test
  void testKeyLoader() {
    Assertions.assertThatCode(
            () -> {
              ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
              try (InputStream privateKeyStream =
                      contextClassLoader.getResourceAsStream("private_key.pem");
                  InputStream publicKeyStream =
                      contextClassLoader.getResourceAsStream("public_key.pem")) {
                PrivateKey privateKey = KeyLoader.loadPrivateKey(privateKeyStream);
                PublicKey publicKey = KeyLoader.loadPublicKey(publicKeyStream);
                Assertions.assertThat(publicKey).isNotNull();
                Assertions.assertThat(privateKey).isNotNull();
              }
            })
        .doesNotThrowAnyException();
  }
}
