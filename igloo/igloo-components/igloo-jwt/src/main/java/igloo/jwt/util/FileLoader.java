package igloo.jwt.util;

import com.google.common.io.CharStreams;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import org.jspecify.annotations.Nullable;

public interface FileLoader {

  @Nullable InputStream load();

  default String readAsString() throws IOException {
    try (InputStream is = load()) {
      if (is == null) {
        throw new FileNotFoundException("Resource not found.");
      }
      return CharStreams.toString(new InputStreamReader(is, StandardCharsets.UTF_8));
    }
  }
}
