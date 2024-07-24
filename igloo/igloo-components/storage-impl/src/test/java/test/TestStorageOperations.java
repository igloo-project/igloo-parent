package test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import io.github.netmikey.logunit.api.LogCapturer;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.igloo.storage.impl.StorageOperations;
import org.igloo.storage.model.StorageUnit;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.api.io.TempDir;
import org.slf4j.event.Level;

class TestStorageOperations {
  @RegisterExtension
  private LogCapturer logs = LogCapturer.create().captureForType(StorageOperations.class);

  private StorageOperations storage = new StorageOperations();

  /**
   * {@link StorageOperations#removePhysicalFile(String, Long, Path)} triggers an exception for an
   * invalid path (null, relative, directory).
   */
  @Test
  void testRemovePhysicalFilesInvalidArgs(@TempDir Path tempDir) {
    assertThatCode(() -> storage.removePhysicalFile(null, 0l, null))
        .isInstanceOf(NullPointerException.class)
        .hasMessageContaining("Path must not be null");
    assertThatCode(() -> storage.removePhysicalFile(null, 0l, Path.of(""))) // NOSONAR
        .isInstanceOf(IllegalStateException.class)
        .hasMessageContaining("is not absolute");
    assertThatCode(() -> storage.removePhysicalFile(null, 0l, tempDir)) // NOSONAR
        .isInstanceOf(IllegalStateException.class)
        .hasMessageContaining("is a directory, not a file");
  }

  /**
   * {@link StorageOperations#removePhysicalFile(String, Long, Path)} triggers a WARN is file is
   * already missing.
   */
  @Test
  void testRemovePhysicalFilesMissingFile(@TempDir Path tempDir) {
    assertThatCode(
            () ->
                storage.removePhysicalFile(
                    null, 0l, tempDir.resolve("not existing file"))) // NOSONAR
        .doesNotThrowAnyException();
    logs.assertContains(e -> Level.WARN.equals(e.getLevel()), "cannot be removed - already absent");
  }

  /** {@link StorageOperations#removePhysicalFile(String, Long, Path)} removes target file. */
  @Test
  void testRemovePhysicalFiles(@TempDir Path tempDir) throws IOException {
    Path file = tempDir.resolve("dir1/dir2/file");
    FileUtils.createParentDirectories(file.toFile());
    if (!file.toFile().createNewFile()) {
      throw new IllegalStateException("Error creating context: file cannot be created");
    }
    storage.removePhysicalFile(null, 0l, file);
    assertThat(file).doesNotExist();
    assertThat(file.getParent()).exists();
  }

  /**
   * {@link StorageOperations#copy(InputStream, Path)} triggers an exception for an invalid target
   * path (null, directory, relative, already existing file).
   */
  @Test
  void copyInvalidPath(@TempDir Path tempDir) throws IOException {
    InputStream inputStream = InputStream.nullInputStream();
    assertThatCode(() -> storage.copy(inputStream, null))
        .isInstanceOf(NullPointerException.class)
        .hasMessageContaining("Path must not be null");
    assertThatCode(() -> storage.copy(inputStream, Path.of(""))) // NOSONAR
        .isInstanceOf(IllegalStateException.class)
        .hasMessageContaining("is not absolute");
    assertThatCode(() -> storage.copy(inputStream, tempDir)) // NOSONAR
        .isInstanceOf(IllegalStateException.class)
        .hasMessageContaining("is a directory, not a file");
    Path file = tempDir.resolve("dir1/dir2/file");
    FileUtils.createParentDirectories(file.toFile());
    if (!file.toFile().createNewFile()) {
      throw new IllegalStateException("Error creating context: file cannot be created");
    }
    assertThatCode(() -> storage.copy(inputStream, file))
        .isInstanceOf(IllegalStateException.class)
        .hasMessageContaining("already exists and is a file");
  }

  /** {@link StorageOperations#copy(InputStream, Path)} triggers exception for an invalid stream. */
  @Test
  void copyInvalidInputStream() {
    assertThatCode(() -> storage.copy(null, null))
        .isInstanceOf(NullPointerException.class)
        .hasMessageContaining("InputStream must not be null");
  }

  /**
   * {@link StorageOperations#copy(InputStream, Path)} create a file with the inputStream content.
   */
  @Test
  void copy(@TempDir Path tempDir) throws IOException {
    InputStream inputStream =
        new ByteArrayInputStream("file content".getBytes(StandardCharsets.UTF_8));
    Path file = tempDir.resolve("dir1/dir2/file");
    storage.copy(inputStream, file);
    assertThat(file).exists().content().isEqualTo("file content");
  }

  /** {@link StorageOperations#copy(InputStream, Path)} allow parent path to already exists. */
  @Test
  void copyParentPathAlreadyExists(@TempDir Path tempDir) throws IOException {
    InputStream inputStream =
        new ByteArrayInputStream("file content".getBytes(StandardCharsets.UTF_8));
    Path file = tempDir.resolve("dir1/dir2/file");
    FileUtils.forceMkdirParent(file.toFile());
    storage.copy(inputStream, file);
    assertThat(file).exists().content().isEqualTo("file content");
  }

  /**
   * {@link StorageOperations#getFile(Path, boolean)} returns a valid {@link File} object (existing,
   * readable).
   */
  @Test
  void getFile(@TempDir Path tempDir) throws IOException {
    Path file = tempDir.resolve("dir1/dir2/file");
    FileUtils.createParentDirectories(file.toFile());
    if (!file.toFile().createNewFile()) {
      throw new IllegalStateException("Error creating context: file cannot be created");
    }
    assertThat(storage.getFile(file.toAbsolutePath(), true))
        .isNotNull()
        .exists()
        .isReadable()
        .isFile()
        .isAbsolute();
  }

  /**
   * {@link StorageOperations#getFile(Path, boolean)} throws a {@link FileNotFoundException} for a
   * missing/not-readable file.
   */
  @Test
  void getFileMissing(@TempDir Path tempDir) throws IOException {
    Path file = tempDir.resolve("dir1/dir2/file");
    FileUtils.createParentDirectories(file.toFile());
    assertThatCode(() -> storage.getFile(file.toAbsolutePath(), true))
        .isInstanceOf(FileNotFoundException.class)
        .hasMessageContaining(file.toString());
  }

  /**
   * {@link StorageOperations#getFile(Path, boolean)} doest not throw an exception on missing file
   * if check is disabled.
   */
  @Test
  void getFileMissingNoCheck(@TempDir Path tempDir) throws IOException {
    Path file = tempDir.resolve("dir1/dir2/file");
    FileUtils.createParentDirectories(file.toFile());
    assertThat(storage.getFile(file.toAbsolutePath(), false))
        .isNotNull()
        .doesNotExist()
        .isAbsolute();
  }

  /**
   * {@link StorageOperations#getFile(Path)} throws an exception if target file is not valid
   * (directory, relative path, null).
   */
  @Test
  void getFileInvalidArgs(@TempDir Path tempDir) throws IOException {
    assertThatCode(() -> storage.getFile(null, false))
        .isInstanceOf(NullPointerException.class)
        .hasMessageContaining("Path must not be null");
    assertThatCode(() -> storage.getFile(Path.of(""), true)) // NOSONAR
        .isInstanceOf(IllegalStateException.class)
        .hasMessageContaining("is not absolute");
    assertThatCode(() -> storage.getFile(tempDir, true)) // NOSONAR
        .isInstanceOf(IllegalStateException.class)
        .hasMessageContaining("is a directory, not a file");
  }

  /** Check {@link StorageOperations#checksum(Path)} result for an existing file. */
  @Test
  void checksum(@TempDir Path tempDir) throws IOException {
    Path file = tempDir.resolve("dir1/dir2/file");
    FileUtils.createParentDirectories(file.toFile());
    if (!file.toFile().createNewFile()) {
      throw new IllegalStateException("Error creating context: file cannot be created");
    }
    try (FileOutputStream fos = new FileOutputStream(file.toFile());
        ByteArrayInputStream bais =
            new ByteArrayInputStream("blabla".getBytes(StandardCharsets.UTF_8))) {
      IOUtils.copy(bais, fos);
    }
    assertThat(storage.checksum(file))
        .isEqualTo("ccadd99b16cd3d200c22d6db45d8b6630ef3d936767127347ec8a76ab992c2ea");
  }

  /**
   * {@link StorageOperations#checksum(Path)} throws an exception for an invalid path (null,
   * relative, directory)
   */
  @Test
  void checksumInvalidPath(@TempDir Path tempDir) {
    assertThatCode(() -> storage.checksum(null))
        .isInstanceOf(NullPointerException.class)
        .hasMessageContaining("Path must not be null");
    assertThatCode(() -> storage.checksum(Path.of(""))) // NOSONAR
        .isInstanceOf(IllegalStateException.class)
        .hasMessageContaining("is not absolute");
    assertThatCode(() -> storage.checksum(tempDir)) // NOSONAR
        .isInstanceOf(IllegalStateException.class)
        .hasMessageContaining("is a directory, not a file");
  }

  /**
   * {@link StorageOperations#checksum(Path)} throws a {@link FileNotFoundException} for a missing
   * or unreadable file.
   */
  @Test
  void checksumMissing(@TempDir Path tempDir) throws IOException {
    Path file = tempDir.resolve("dir1/dir2/file");
    FileUtils.createParentDirectories(file.toFile());
    assertThatCode(() -> storage.checksum(file.toAbsolutePath()))
        .isInstanceOf(FileNotFoundException.class)
        .hasMessageContaining(file.toString());
  }

  /**
   * {@link StorageOperations#listUnitContent(StorageUnit)} returns an empty collection for an empty
   * directory.
   */
  @Test
  void listUnitContentEmpty(@TempDir Path tempDir) {
    StorageUnit unit = mock(StorageUnit.class);
    when(unit.getPath()).thenReturn(tempDir.toString());
    assertThat(storage.listUnitContent(unit)).isEmpty();
  }

  /**
   * {@link StorageOperations#listUnitContent(StorageUnit)} return an empty collection for a missing
   * directory.
   */
  @Test
  void listUnitContentNotExisting(@TempDir Path tempDir) {
    Path notExisting = tempDir.resolve("not existing");
    StorageUnit unit = mock(StorageUnit.class);
    when(unit.getPath()).thenReturn(notExisting.toString());
    assertThat(storage.listUnitContent(unit)).isEmpty();
  }

  /** {@link StorageOperations#listUnitContent(StorageUnit)} list only files from directory. */
  @Test
  void listUnitContent(@TempDir Path tempDir) throws IOException {
    Path path1 = tempDir.resolve("path1");
    Path path2Path3 = tempDir.resolve("path2/path3");
    Path path1File = path1.resolve("file");
    Path path4Path5 = tempDir.resolve("path4/path5");
    Path path5File = path4Path5.resolve("file");
    FileUtils.forceMkdir(path2Path3.toFile());
    FileUtils.forceMkdir(path1.toFile());
    FileUtils.forceMkdir(path4Path5.toFile());
    path1File.toFile().createNewFile();
    path5File.toFile().createNewFile();
    StorageUnit unit = mock(StorageUnit.class);
    when(unit.getPath()).thenReturn(tempDir.toString());
    assertThat(storage.listUnitContent(unit)).containsExactlyInAnyOrder(path1File, path5File);
  }
}
