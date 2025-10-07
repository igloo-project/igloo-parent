package igloo.jwt.util;

import com.pivovarit.function.ThrowingFunction;
import java.io.FileInputStream;
import java.util.Optional;

/**
 * Various utilities for file loading. File may be loaded:
 *
 * <ul>
 *   <li>From a path, optionally referencing file: or classpath: scheme
 *   <li><code>file:</code> scheme may be absolute (<code>file:/root/file</code>) or relative (
 *       <code>file:relative/file</code>)
 *   <li><code>classpath:</code> are alway absolute
 *   <li>{@link #fromUrl(String)}: path with no schema are interpreted as resource path
 *   <li>{@link #fromFile(String)}: path are always interpreted as file path. Scheme is optional
 *   <li>{@link #fromResource(String)}: path are always interpreted as resource path. Scheme is
 *       optional
 *   <li>{@link #fromResource(Class, String)}: allows resource loading relative to a class file
 * </ul>
 */
public class FileLoaders {
  private FileLoaders() {}

  private static final String SCHEME_FILE = "file:";
  private static final String SCHEME_CLASSPATH = "classpath:";

  public static FileLoader fromFile(final String path) {
    String filePath = path;
    if (!filePath.startsWith(SCHEME_FILE)) {
      filePath = SCHEME_FILE + path;
    }
    return fromUrl(filePath);
  }

  public static FileLoader fromResource(final String path) {
    if (path.startsWith(SCHEME_CLASSPATH)) {
      throw new IllegalStateException("path %s is forbidden for resource loading".formatted(path));
    }
    return fromUrl(path);
  }

  public static FileLoader fromResource(Class<?> context, String path) {
    return fromUrl(context.getPackage().getName().replace(".", "/") + "/" + path);
  }

  public static FileLoader fromUrl(String path) {
    return () -> {
      String filePath = path;
      if (filePath.startsWith(SCHEME_FILE)) {
        filePath = filePath.substring(SCHEME_FILE.length());
        return Optional.of(filePath)
            .map(ThrowingFunction.sneaky(FileInputStream::new))
            .orElseThrow(() -> new RuntimeException("Opening resource %s failed".formatted(path)));
      }

      // handle classpath resource case
      if (filePath.startsWith(SCHEME_CLASSPATH)) {
        filePath = filePath.substring(SCHEME_CLASSPATH.length());
      }
      while (filePath.startsWith("/")) {
        filePath = filePath.substring(filePath.indexOf("/") + 1);
      }
      return Thread.currentThread().getContextClassLoader().getResourceAsStream(filePath);
    };
  }
}
