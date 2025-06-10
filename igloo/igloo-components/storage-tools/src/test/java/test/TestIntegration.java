package test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import igloo.test.listener.postgresql.PsqlTestContainerConfiguration;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import org.igloo.storage.api.IStorageService;
import org.igloo.storage.impl.DatabaseOperations;
import org.igloo.storage.model.Fichier;
import org.igloo.storage.model.atomic.FichierStatus;
import org.igloo.storage.model.atomic.StorageUnitCheckType;
import org.igloo.storage.tools.StorageToolsMain;
import org.igloo.storage.tools.util.EntityManagerHelper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.jdbc.Sql;
import test.model.FichierType;
import test.model.StorageUnitType;

@TestExecutionListeners(mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS)
@Sql("classpath:sql/init.sql")
@SpringBootTest
@SpringBootConfiguration
@Import({PsqlTestContainerConfiguration.class, StorageToolsMain.class})
class TestIntegration {
  @Autowired ConfigurableApplicationContext applicationContext;

  @Autowired EntityManagerHelper entityManagerHelper;

  @Autowired DatabaseOperations databaseOperations;

  @Autowired IStorageService storageService;

  /** Move some files by ids. Check move and missing behavior. */
  @Test
  void testIntegration(@TempDir Path tempDir) throws IOException {
    Path configurationFile = tempDir.resolve("configuration.properties");
    configurationFile.toFile().createNewFile();

    // create source storageunit, override path
    Path sourceFolder = tempDir.resolve("source");
    sourceFolder.toFile().mkdir();

    entityManagerHelper.doWithReadWriteTransaction(
        e -> {
          return storageService.createStorageUnit(
              StorageUnitType.SOURCE, StorageUnitCheckType.NONE, sourceFolder.toString());
        });
    Fichier fichier1 =
        entityManagerHelper.doWithReadWriteTransaction(
            e ->
                storageService.addFichier(
                    "test", FichierType.TYPE1, new ByteArrayInputStream("fichier1".getBytes())));
    Fichier fichier2Missing =
        entityManagerHelper.doWithReadWriteTransaction(
            e ->
                storageService.addFichier(
                    "test", FichierType.TYPE1, new ByteArrayInputStream("fichier2".getBytes())));
    Fichier fichier3AlreadyMoved =
        entityManagerHelper.doWithReadWriteTransaction(
            e ->
                storageService.addFichier(
                    "test", FichierType.TYPE1, new ByteArrayInputStream("fichier3".getBytes())));
    Fichier fichier4 =
        entityManagerHelper.doWithReadWriteTransaction(
            e ->
                storageService.addFichier(
                    "test", FichierType.TYPE1, new ByteArrayInputStream("fichier4".getBytes())));
    Fichier fichierIgnored =
        entityManagerHelper.doWithReadWriteTransaction(
            e ->
                storageService.addFichier(
                    "test", FichierType.TYPE1, new ByteArrayInputStream("fichier4".getBytes())));

    File fichier1Original = storageService.getFile(fichier1);
    File fichier2Original = storageService.getFile(fichier2Missing);
    File fichier3Original = storageService.getFile(fichier3AlreadyMoved);
    File fichier4Original = storageService.getFile(fichier4);
    File fichierIgnoredOriginal = storageService.getFile(fichierIgnored);

    // drop file
    storageService.getFile(fichier2Missing).delete();
    // move file to expected location
    File fichier3File = storageService.getFile(fichier3AlreadyMoved);
    String fichier3Path = fichier3File.getPath();
    String fichier3TargetPath = fichier3Path.replace("source", "target");
    fichier3File.renameTo(new File(fichier3TargetPath));

    Path targetFolder = tempDir.resolve("target");
    targetFolder.toFile().mkdir();
    StorageToolsMain.APPLICATION_CONTEXT = applicationContext;
    StorageToolsMain.main(
        new String[] {
          "-c",
          configurationFile.toString(),
          "archive",
          "--ids",
          fichier1.getId().toString(),
          "--ids",
          fichier2Missing.getId().toString(),
          "--ids",
          fichier3AlreadyMoved.getId().toString(),
          "--ids",
          fichier4.getId().toString(),
          targetFolder.toString(),
          StorageUnitType.class.getName() + "." + StorageUnitType.TARGET.name()
        });

    // available files are updated
    fichier1 = storageService.getFichierById(fichier1.getId());
    assertThat(fichier1.getStorageUnit().getPath()).isEqualTo(targetFolder.toString());
    assertThat(storageService.getFile(fichier1, false, false)).exists();
    assertThat(storageService.getFile(fichier1, false, false).getPath()).contains("target");
    assertThat(fichier1.getStatus()).isEqualTo(FichierStatus.UNAVAILABLE);
    assertThat(fichier1Original).doesNotExist();
    fichier4 = storageService.getFichierById(fichier4.getId());
    assertThat(fichier4.getStorageUnit().getPath()).isEqualTo(targetFolder.toString());
    assertThat(storageService.getFile(fichier4, false, false)).exists();
    assertThat(storageService.getFile(fichier4, false, false).getPath()).contains("target");
    assertThat(fichier4.getStatus()).isEqualTo(FichierStatus.UNAVAILABLE);
    assertThat(fichier4Original).doesNotExist();

    // missing file are updated
    Fichier fichier2 = storageService.getFichierById(fichier2Missing.getId());
    assertThat(fichier2.getStorageUnit().getPath()).isEqualTo(targetFolder.toString());
    assertThatCode(() -> storageService.getFile(fichier2))
        .isInstanceOf(FileNotFoundException.class);
    assertThat(storageService.getFile(fichier2, false, false).getPath()).contains("target");
    assertThat(fichier2.getStatus()).isEqualTo(FichierStatus.UNAVAILABLE);
    assertThat(fichier2Original).doesNotExist();

    // already moved files are updated
    fichier3AlreadyMoved = storageService.getFichierById(fichier3AlreadyMoved.getId());
    assertThat(storageService.getFile(fichier3AlreadyMoved, false, false)).exists();
    assertThat(storageService.getFile(fichier3AlreadyMoved, false, false).getPath())
        .contains("target");
    assertThat(fichier3AlreadyMoved.getStatus()).isEqualTo(FichierStatus.UNAVAILABLE);
    assertThat(fichier3Original).doesNotExist();

    // ignored file remains untouched
    fichierIgnored = storageService.getFichierById(fichierIgnored.getId());
    assertThat(fichierIgnored.getStorageUnit().getPath()).isEqualTo(sourceFolder.toString());
    assertThat(storageService.getFile(fichierIgnored, false, false)).exists();
    assertThat(storageService.getFile(fichierIgnored, false, false).getPath()).contains("source");
    assertThat(fichierIgnored.getStatus()).isEqualTo(FichierStatus.TRANSIENT);
    assertThat(fichierIgnoredOriginal).exists();
  }
}
