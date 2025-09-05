package test;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.EnumSet;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.logging.LogManager;
import org.assertj.core.matcher.AssertionMatcher;
import org.igloo.storage.impl.DatabaseOperations;
import org.igloo.storage.impl.StorageOperations;
import org.igloo.storage.impl.StorageService;
import org.igloo.storage.model.Fichier;
import org.igloo.storage.model.StorageUnit;
import org.igloo.storage.model.atomic.IFichierType;
import org.igloo.storage.model.atomic.IStorageUnitType;
import org.igloo.storage.model.atomic.StorageUnitCheckType;
import org.mockito.hamcrest.MockitoHamcrest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.core.Ordered;
import org.springframework.orm.jpa.EntityManagerFactoryUtils;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.interceptor.DefaultTransactionAttribute;
import org.springframework.transaction.support.TransactionTemplate;
import test.model.StorageUnitType;

abstract class AbstractTest {
  static {
    // jboss-logging -> slf4j
    System.setProperty("org.jboss.logging.provider", "slf4j");
    // enable JUL logging
    try {
      ByteArrayInputStream is = new ByteArrayInputStream(".level=ALL".getBytes());
      LogManager.getLogManager().readConfiguration(is);
    } catch (SecurityException | IOException e) {
      throw new IllegalStateException(e);
    }
    SLF4JBridgeHandler.install();
  }

  protected static final Logger LOGGER = LoggerFactory.getLogger(AbstractTest.class);

  protected static final String FILENAME = "filename.txt";
  protected static final String FILE_CONTENT = "blabla";
  // checksum from 'echo -n "blabla" | sha256sum'
  protected static final String FILE_CHECKSUM_SHA_256 =
      "ccadd99b16cd3d200c22d6db45d8b6630ef3d936767127347ec8a76ab992c2ea";
  protected static final long FILE_SIZE = 6L;

  protected StorageService storageService;
  protected TransactionTemplate transactionTemplate;

  public static <T> T argThat(Consumer<T> assertions) {
    return MockitoHamcrest.argThat(
        new AssertionMatcher<T>() {
          @Override
          public void assertion(T actual) throws AssertionError {
            assertions.accept(actual);
          }
        });
  }

  void init(
      EntityManagerFactory entityManagerFactory,
      Path tempDir,
      StorageOperations storageOperations,
      DatabaseOperations databaseOperations) {
    this.storageService =
        new StorageService(
            Ordered.LOWEST_PRECEDENCE,
            databaseOperations,
            Set.<IStorageUnitType>copyOf(EnumSet.allOf(StorageUnitType.class)),
            storageOperations,
            () -> tempDir);
  }

  StorageUnit initStorageUnit(EntityManagerFactory entityManagerFactory) {
    Supplier<StorageUnit> action =
        () -> storageService.createStorageUnit(StorageUnitType.TYPE_1, StorageUnitCheckType.NONE);
    return doInWriteTransaction(entityManagerFactory, action);
  }

  <T> T doInWriteTransaction(EntityManagerFactory entityManagerFactory, Supplier<T> action) {
    PlatformTransactionManager transactionManager = new JpaTransactionManager(entityManagerFactory);
    transactionTemplate = new TransactionTemplate(transactionManager, writeTransactionAttribute());
    return transactionTemplate.execute((t) -> action.get());
  }

  <T> T doInWriteTransactionEntityManager(
      EntityManagerFactory entityManagerFactory, Function<EntityManager, T> action) {
    PlatformTransactionManager transactionManager = new JpaTransactionManager(entityManagerFactory);
    transactionTemplate = new TransactionTemplate(transactionManager, writeTransactionAttribute());
    return transactionTemplate.execute(
        (t) ->
            action.apply(
                EntityManagerFactoryUtils.getTransactionalEntityManager(entityManagerFactory)));
  }

  <T> T doInReadTransaction(EntityManagerFactory entityManagerFactory, Supplier<T> action) {
    PlatformTransactionManager transactionManager = new JpaTransactionManager(entityManagerFactory);
    transactionTemplate = new TransactionTemplate(transactionManager, readTransactionAttribute());
    return transactionTemplate.execute((t) -> action.get());
  }

  <T> T doInReadTransactionEntityManager(
      EntityManagerFactory entityManagerFactory, Function<EntityManager, T> action) {
    PlatformTransactionManager transactionManager = new JpaTransactionManager(entityManagerFactory);
    transactionTemplate = new TransactionTemplate(transactionManager, readTransactionAttribute());
    return transactionTemplate.execute(
        (t) ->
            action.apply(
                EntityManagerFactoryUtils.getTransactionalEntityManager(entityManagerFactory)));
  }

  protected Fichier createFichier(String filename, IFichierType fichierType, String fileContent) {
    return createFichier(filename, fichierType, fileContent, (f) -> {});
  }

  protected Fichier createFichier(
      String filename,
      IFichierType fichierType,
      String fileContent,
      Consumer<Fichier> postCreationAction) {
    return transactionTemplate.execute(
        (t) -> {
          Fichier fichier = null;
          try (InputStream bais =
              new ByteArrayInputStream(fileContent.getBytes(StandardCharsets.UTF_8))) {
            fichier = storageService.addFichier(filename, fichierType, bais);
            return fichier;
          } catch (IOException e) {
            throw new IllegalStateException(e);
          } finally {
            postCreationAction.accept(fichier);
          }
        });
  }

  protected DefaultTransactionAttribute writeTransactionAttribute() {
    DefaultTransactionAttribute ta = new DefaultTransactionAttribute();
    ta.setReadOnly(false);
    return ta;
  }

  protected DefaultTransactionAttribute readTransactionAttribute() {
    DefaultTransactionAttribute ta = new DefaultTransactionAttribute();
    ta.setReadOnly(true);
    return ta;
  }
}
