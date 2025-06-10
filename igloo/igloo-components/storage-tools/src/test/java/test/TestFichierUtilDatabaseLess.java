package test;

import static java.util.stream.LongStream.rangeClosed;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import org.assertj.core.api.Assertions;
import org.igloo.storage.model.Fichier;
import org.igloo.storage.model.StorageUnit;
import org.igloo.storage.tools.ConfigurationException;
import org.igloo.storage.tools.commands.ExecutorMixin;
import org.igloo.storage.tools.util.ArchivingProgressMonitor;
import org.igloo.storage.tools.util.EntityManagerHelper;
import org.igloo.storage.tools.util.FichierUtil;
import org.igloo.storage.tools.util.FichierUtil.ExecutorCallback;
import org.igloo.storage.tools.util.FichierUtil.MoveResult;
import org.igloo.storage.tools.util.FichierUtil.RunMode;
import org.igloo.storage.tools.util.FichierUtil.SwitchToUnavailable;
import org.igloo.storage.tools.util.FsUtil;
import org.igloo.storage.tools.util.action.DbCheckOrCreateStorageUnitAction;
import org.igloo.storage.tools.util.action.DbListFichiersAction;
import org.igloo.storage.tools.util.action.DbLoadFichierIdsAction;
import org.igloo.storage.tools.util.action.DbMoveFichiersAction;
import org.igloo.storage.tools.util.action.IDbAction;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.ArgumentCaptor;

class TestFichierUtilDatabaseLess {
  /** Loading ids from collection */
  @Test
  void loadFichierIdsFromQuery_fromCollection() {
    EntityManagerHelper helper = mock(EntityManagerHelper.class);
    FichierUtil fichierUtil = new FichierUtil(helper, null);

    fichierUtil.loadFichierIdsFromArgs(null, List.of(1l));

    // bdd is not called
    verifyNoInteractions(helper);
  }

  /** Loading ids from collection */
  @Test
  void loadFichierIdsFromQuery_fromCollection_deduplicate() {
    EntityManagerHelper helper = mock(EntityManagerHelper.class);
    FichierUtil fichierUtil = new FichierUtil(helper, null);

    assertThat(fichierUtil.loadFichierIdsFromArgs(null, List.of(1l, 1l))).containsExactly(1l);

    // bdd is not called
    verifyNoInteractions(helper);
  }

  /** Loading ids from collection */
  @Test
  void loadFichierIdsFromQuery_fromCollection_ordered() {
    EntityManagerHelper helper = mock(EntityManagerHelper.class);
    FichierUtil fichierUtil = new FichierUtil(helper, null);

    assertThat(fichierUtil.loadFichierIdsFromArgs(null, List.of(2l, 1l, 3l, 1l, 4l)))
        .containsExactly(2l, 1l, 3l, 4l);

    // bdd is not called
    verifyNoInteractions(helper);
  }

  /**
   * Loading ids from query
   *
   * @throws IOException
   */
  @SuppressWarnings("unchecked")
  @Test
  void loadFichierIdsFromQuery_fromQuery(@TempDir Path tempDir) throws IOException {
    Path queryFile = tempDir.resolve("test.sql");
    Files.writeString(queryFile, "SELECT id FROM Fichier;");
    EntityManagerHelper helper = mock(EntityManagerHelper.class);
    FichierUtil fichierUtil = new FichierUtil(helper, null);

    // mock bdd call
    when(helper.doWithReadOnlyTransaction(any(IDbAction.class)))
        .thenReturn(List.of(2l, 1l, 3l, 1l, 4l));
    assertThat(fichierUtil.loadFichierIdsFromArgs(queryFile, List.of()))
        .containsExactly(2l, 1l, 3l, 4l);

    ArgumentCaptor<IDbAction<List<Long>>> captor = ArgumentCaptor.forClass(IDbAction.class);
    verify(helper, only()).doWithReadOnlyTransaction(captor.capture());
    assertThat(captor.getValue()).isInstanceOf(DbLoadFichierIdsAction.class);
  }

  @Test
  void checkOrCreateStorageUnit_absolutePath() {
    EntityManagerHelper helper = mock(EntityManagerHelper.class);
    FichierUtil fichierUtil = new FichierUtil(helper, null);

    assertThatCode( // NOSONAR
            () -> fichierUtil.checkOrCreateStorageUnit(Path.of("relativePath"), null))
        .isInstanceOf(ConfigurationException.class)
        .hasMessage("Path relativePath must be an absolute path.");
    ;
  }

  @Test
  void checkOrCreateStorageUnit() {
    EntityManagerHelper helper = mock(EntityManagerHelper.class);
    FichierUtil fichierUtil = new FichierUtil(helper, null);

    // mock bdd call
    StorageUnit storageUnit = new StorageUnit();
    when(helper.doWithReadWriteTransaction(any(DbCheckOrCreateStorageUnitAction.class)))
        .thenReturn(storageUnit);
    assertThat(fichierUtil.checkOrCreateStorageUnit(Path.of("/path"), null)).isSameAs(storageUnit);
    verify(helper, only()).doWithReadWriteTransaction((IDbAction<?>) any());
  }

  @Test
  void processMove() {
    FichierUtil fichierUtil = new FichierUtil(null, null);
    ExecutorCallback callback = mock(ExecutorCallback.class);
    ExecutorMixin executor = new ExecutorMixin();
    executor.parallelism = 3;
    executor.batchSize = 10;
    StorageUnit target = new StorageUnit();
    @SuppressWarnings("unchecked")
    ArgumentCaptor<List<Long>> captor = ArgumentCaptor.forClass(List.class);
    doNothing()
        .when(callback)
        .processMovePartition(
            any(), eq(RunMode.REAL), eq(SwitchToUnavailable.YES), eq(target), captor.capture());
    fichierUtil.processMove(
        executor,
        RunMode.REAL,
        SwitchToUnavailable.YES,
        target,
        rangeClosed(0l, 30l).boxed().toList(),
        callback);
    Assertions.assertThat(captor.getAllValues())
        .contains(
            rangeClosed(0l, 9l).boxed().toList(),
            rangeClosed(10l, 19l).boxed().toList(),
            rangeClosed(20l, 29l).boxed().toList(),
            List.of(30l));
  }

  @Test
  void processMovePartition_dryRun() {
    processMovePartition(RunMode.DRY_RUN);
  }

  @Test
  void processMovePartition_real() {
    processMovePartition(RunMode.REAL);
  }

  void processMovePartition(RunMode runMode) {
    FsUtil fsUtil = mock(FsUtil.class);
    EntityManagerHelper helper = mock(EntityManagerHelper.class);
    FichierUtil fichierUtil = new FichierUtil(helper, fsUtil);
    ArchivingProgressMonitor progressMonitor = mock(ArchivingProgressMonitor.class);
    StorageUnit target = new StorageUnit();

    Fichier fichier1 = new Fichier();
    fichier1.setId(1l);
    Fichier fichier2 = new Fichier();
    fichier2.setId(2l);

    // Fichier entity loading (read)
    when(helper.doWithReadOnlyTransaction(any(DbListFichiersAction.class)))
        .thenReturn(List.of(fichier1, fichier2));
    // Move operation
    ArgumentCaptor<Fichier> captor = ArgumentCaptor.forClass(Fichier.class);
    when(fsUtil.move(eq(progressMonitor), eq(runMode), eq(target), captor.capture()))
        .thenReturn(MoveResult.MOVED);
    // Fichier entity update
    // Move operation (write)
    ArgumentCaptor<DbMoveFichiersAction> resultCaptor =
        ArgumentCaptor.forClass(DbMoveFichiersAction.class);
    if (RunMode.DRY_RUN.equals(runMode)) {
      // dry-run -> read-only
      when(helper.doWithReadOnlyTransaction(resultCaptor.capture())).thenReturn(true);
    } else {
      when(helper.doWithReadWriteTransaction(resultCaptor.capture())).thenReturn(true);
    }

    fichierUtil.processMovePartition(
        progressMonitor, runMode, SwitchToUnavailable.YES, target, List.of(1l, 2l));

    verify(helper, times(1)).doWithReadOnlyTransaction(any(DbListFichiersAction.class));
    if (RunMode.DRY_RUN.equals(runMode)) {
      // dry-run -> read-only
      verify(helper, times(1)).doWithReadOnlyTransaction(any(DbMoveFichiersAction.class));
    } else {
      verify(helper, times(1)).doWithReadWriteTransaction(any(DbMoveFichiersAction.class));
    }
    verify(fsUtil, times(2)).move(any(), any(), any(), any());
    assertThat(captor.getAllValues()).contains(fichier1, fichier2);
    assertThat(resultCaptor.getValue().getResults())
        .containsAllEntriesOf(Map.of(1l, MoveResult.MOVED, 2l, MoveResult.MOVED));
    verifyNoMoreInteractions(helper, fsUtil);
  }
}
