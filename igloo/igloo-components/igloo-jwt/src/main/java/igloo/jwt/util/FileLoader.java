package igloo.jwt.util;

import com.google.common.io.CharStreams;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public interface FileLoader {

  abstract InputStream load();

  default String readAsString() throws IOException {
    try (InputStream is = load()) {
      return CharStreams.toString(new InputStreamReader(is, StandardCharsets.UTF_8));
    }
  }
}
