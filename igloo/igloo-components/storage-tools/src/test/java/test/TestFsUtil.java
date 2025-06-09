package test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.nio.file.Path;
import org.igloo.storage.model.Fichier;
import org.igloo.storage.model.StorageUnit;
import org.igloo.storage.model.atomic.IStorageUnitType;
import org.igloo.storage.tools.util.ArchivingProgressMonitor;
import org.igloo.storage.tools.util.FichierUtil.MoveResult;
import org.igloo.storage.tools.util.FichierUtil.RunMode;
import org.igloo.storage.tools.util.FsUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * Check file operation and {@link MoveResult} feedback. We setup a source file and its path and a
 * target {@link StorageUnit}. We check {@link MoveResult} accordingly to file existence and
 * location.
 */
class TestFsUtil {
  StorageUnit target = new StorageUnit();
  Fichier fichier = new Fichier();
  @TempDir Path tempDir;

  @BeforeEach
  void setup() {
    // setup source Fichier so that path can be resolved to source/subfolder-source/fichier-source
    // setup target storageUnit so that path can be resolved to
    // target/subfolder-target/fichier-target

    // fichier
    StorageUnit sourceStorageUnit = new StorageUnit();
    fichier = new Fichier();
    fichier.setStorageUnit(sourceStorageUnit);
    IStorageUnitType sourceType = mock(IStorageUnitType.class);
    sourceStorageUnit.setType(sourceType);
    sourceStorageUnit.setPath(tempDir.resolve("source").toString());

    // storage unit target
    target = new StorageUnit();
    target.setPath(tempDir.resolve("target").toString());
    IStorageUnitType targetType = mock(IStorageUnitType.class);
    target.setType(targetType);

    when(sourceType.getFichierPathStrategy()).thenReturn(f -> "subfolder-source/fichier-source");
    when(targetType.getFichierPathStrategy()).thenReturn(f -> "subfolder-target/fichier-target");
  }

  @Test
  void testMoved() throws IOException {
    FsUtil fsUtil = new FsUtil();
    ArchivingProgressMonitor monitor = mock(ArchivingProgressMonitor.class);
    tempDir.resolve("source/subfolder-source/fichier-source").getParent().toFile().mkdirs();
    tempDir.resolve("source/subfolder-source/fichier-source").toFile().createNewFile();

    assertThat(fsUtil.move(monitor, RunMode.REAL, target, fichier)).isEqualTo(MoveResult.MOVED);
    verify(monitor, only()).moved();
    assertThat(tempDir.resolve("source/subfolder-source/fichier-source").toFile()).doesNotExist();
    assertThat(tempDir.resolve("target/subfolder-target/fichier-target").toFile()).exists();
  }

  @Test
  void testAlreadyMoved() throws IOException {
    FsUtil fsUtil = new FsUtil();
    ArchivingProgressMonitor monitor = mock(ArchivingProgressMonitor.class);
    tempDir.resolve("target/subfolder-target/fichier-target").getParent().toFile().mkdirs();
    tempDir.resolve("target/subfolder-target/fichier-target").toFile().createNewFile();

    // already moved file
    assertThat(fsUtil.move(monitor, RunMode.REAL, target, fichier))
        .isEqualTo(MoveResult.ALREADY_MOVED);
    verify(monitor, only()).alreadyMoved();
    assertThat(tempDir.resolve("source/subfolder-source/fichier-source").toFile()).doesNotExist();
    assertThat(tempDir.resolve("target/subfolder-target/fichier-target").toFile()).exists();
  }

  @Test
  void testUntouched() throws IOException {
    FsUtil fsUtil = new FsUtil();
    fichier.setStorageUnit(target);
    ArchivingProgressMonitor monitor = mock(ArchivingProgressMonitor.class);
    tempDir.resolve("target/subfolder-target/fichier-target").getParent().toFile().mkdirs();
    tempDir.resolve("target/subfolder-target/fichier-target").toFile().createNewFile();

    // untouched file, counted as already moved
    assertThat(fsUtil.move(monitor, RunMode.REAL, target, fichier)).isEqualTo(MoveResult.UNTOUCHED);
    verify(monitor, only()).alreadyMoved();
    assertThat(tempDir.resolve("source/subfolder-source/fichier-source").toFile()).doesNotExist();
    assertThat(tempDir.resolve("target/subfolder-target/fichier-target").toFile()).exists();
  }

  @Test
  void testUntouched_missing() throws IOException {
    FsUtil fsUtil = new FsUtil();
    fichier.setStorageUnit(target);
    ArchivingProgressMonitor monitor = mock(ArchivingProgressMonitor.class);
    tempDir.resolve("target/subfolder-target/fichier-target").getParent().toFile().mkdirs();

    // untouched file, counted as missing
    assertThat(fsUtil.move(monitor, RunMode.REAL, target, fichier)).isEqualTo(MoveResult.UNTOUCHED);
    verify(monitor, only()).missing(fichier);
    assertThat(tempDir.resolve("source/subfolder-source/fichier-source").toFile()).doesNotExist();
    assertThat(tempDir.resolve("target/subfolder-target/fichier-target").toFile()).doesNotExist();
  }

  @Test
  void testMissing() throws IOException {
    FsUtil fsUtil = new FsUtil();
    ArchivingProgressMonitor monitor = mock(ArchivingProgressMonitor.class);
    tempDir.resolve("target/subfolder-target/fichier-target").getParent().toFile().mkdirs();

    assertThat(fsUtil.move(monitor, RunMode.REAL, target, fichier)).isEqualTo(MoveResult.MISSING);
    assertThat(tempDir.resolve("source/subfolder-source/fichier-source").toFile()).doesNotExist();
    assertThat(tempDir.resolve("target/subfolder-target/fichier-target").toFile()).doesNotExist();
  }

  @Test
  void testMoved_dryRun() throws IOException {
    FsUtil fsUtil = new FsUtil();
    ArchivingProgressMonitor monitor = mock(ArchivingProgressMonitor.class);
    tempDir.resolve("source/subfolder-source/fichier-source").getParent().toFile().mkdirs();
    tempDir.resolve("source/subfolder-source/fichier-source").toFile().createNewFile();

    assertThat(fsUtil.move(monitor, RunMode.DRY_RUN, target, fichier)).isEqualTo(MoveResult.MOVED);
    verify(monitor, only()).moved();
    assertThat(tempDir.resolve("source/subfolder-source/fichier-source").toFile()).exists();
    assertThat(tempDir.resolve("target/subfolder-target/fichier-target").toFile()).doesNotExist();
  }
}
