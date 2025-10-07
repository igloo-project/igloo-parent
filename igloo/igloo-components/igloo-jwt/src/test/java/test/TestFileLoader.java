package test;

import static org.assertj.core.api.Assertions.assertThat;

import com.google.common.io.Files;
import igloo.jwt.util.FileLoader;
import igloo.jwt.util.FileLoaders;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class TestFileLoader {

  @Test
  void testFileLoader_file_absolute(@TempDir Path tempPath) throws IOException {
    Path path = tempPath.resolve("test.txt");
    Files.asCharSink(path.toFile(), StandardCharsets.UTF_8).write("test");

    FileLoader loader = FileLoaders.fromFile(path.toAbsolutePath().toString());
    assertThat(loader.load()).hasBinaryContent("test".getBytes(StandardCharsets.UTF_8));
  }

  @Test
  void testFileLoader_file_relative(@TempDir Path tempPath) throws IOException {
    Path path = tempPath.resolve("test.txt");
    Files.asCharSink(path.toFile(), StandardCharsets.UTF_8).write("test");

    Path currentWorkdir = Path.of("").toAbsolutePath();
    FileLoader loader = FileLoaders.fromFile(currentWorkdir.relativize(path).toString());
    assertThat(loader.load()).hasBinaryContent("test".getBytes(StandardCharsets.UTF_8));
  }

  @Test
  void testFileLoader_resource() {
    FileLoader loader = FileLoaders.fromResource("resource.txt");
    assertThat(loader.load())
        .hasBinaryContent("resource for TestFileLoader".getBytes(StandardCharsets.UTF_8));
  }

  @Test
  void testFileLoader_resource_absolute() {
    FileLoader loader = FileLoaders.fromResource("/resource.txt");
    assertThat(loader.load())
        .hasBinaryContent("resource for TestFileLoader".getBytes(StandardCharsets.UTF_8));
  }

  @Test
  void testFileLoader_resource2() {
    FileLoader loader = FileLoaders.fromResource("test/resource2.txt");
    assertThat(loader.load())
        .hasBinaryContent("other resource for TestFileLoader".getBytes(StandardCharsets.UTF_8));
  }

  @Test
  void testFileLoader_resource2_absolute() {
    FileLoader loader = FileLoaders.fromResource("/test/resource2.txt");
    assertThat(loader.load())
        .hasBinaryContent("other resource for TestFileLoader".getBytes(StandardCharsets.UTF_8));
  }

  @Test
  void testFileLoader_class_resource2() {
    FileLoader loader = FileLoaders.fromResource(TestFileLoader.class, "resource2.txt");
    assertThat(loader.load())
        .hasBinaryContent("other resource for TestFileLoader".getBytes(StandardCharsets.UTF_8));
  }
}
