package igloo.jwt.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.function.Function;
import org.apache.commons.codec.binary.Base64;

/**
 * Key loading from RSA openssl generated keys.
 *
 * <pre>{@code
 * # Generate private key
 * openssl genrsa -out private_key.pem 2048
 * # extract public key
 * openssl rsa -in private_key.pem -pubout -outform PEM -out public_key.pem
 * }</pre>
 */
public class KeyLoader {

  private KeyLoader() {}

  private static <K extends Key> K loadKey(InputStream in, Function<byte[], K> keyParser)
      throws IOException {
    try (BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
      String line;
      StringBuilder content = new StringBuilder();
      while ((line = reader.readLine()) != null) {
        if (!(line.contains("BEGIN") || line.contains("END"))) {
          content.append(line).append('\n');
        }
      }
      byte[] encoded = Base64.decodeBase64(content.toString());
      return keyParser.apply(encoded);
    }
  }

  /** Caller is responsible for closing the provided {@link InputStream}. */
  public static PrivateKey loadPrivateKey(InputStream in)
      throws IOException, NoSuchAlgorithmException {
    KeyFactory keyFactory = KeyFactory.getInstance("RSA");

    return loadKey(
        in,
        bytes -> {
          PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(bytes);
          try {
            return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
          } catch (InvalidKeySpecException e) {
            throw new RuntimeException(e);
          }
        });
  }

  /** Caller is responsible for closing the provided {@link InputStream}. */
  public static PublicKey loadPublicKey(InputStream in)
      throws IOException, NoSuchAlgorithmException {
    KeyFactory keyFactory = KeyFactory.getInstance("RSA");

    return loadKey(
        in,
        bytes -> {
          try {
            X509EncodedKeySpec spec = new X509EncodedKeySpec(bytes);
            return keyFactory.generatePublic(spec);
          } catch (InvalidKeySpecException e) {
            throw new RuntimeException(e);
          }
        });
  }

  /**
   * Use {@link FileLoaders#fromFile(String)}, {@link FileLoaders#fromResource(String)} {@link
   * FileLoaders#fromUrl(String)} to obtain an adequate {@link FileLoader}.
   */
  public static PublicKey loadPublicKey(FileLoader loader)
      throws NoSuchAlgorithmException, IOException {
    try (InputStream stream = loader.load()) {
      return loadPublicKey(stream);
    }
  }

  /**
   * Use {@link FileLoaders#fromFile(String)}, {@link FileLoaders#fromResource(String)} {@link
   * FileLoaders#fromUrl(String)} to obtain an adequate {@link FileLoader}.
   */
  public static PrivateKey loadPrivateKey(FileLoader loader)
      throws NoSuchAlgorithmException, IOException {
    try (InputStream stream = loader.load()) {
      return loadPrivateKey(stream);
    }
  }
}
