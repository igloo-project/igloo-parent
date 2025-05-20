package org.igloo.storage.tools.fake;

import com.google.common.collect.Lists;
import jakarta.persistence.TypedQuery;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.apache.commons.io.FileUtils;
import org.igloo.storage.model.Fichier;
import org.igloo.storage.model.atomic.IFichierPathStrategy;
import org.igloo.storage.tools.EntityManagerHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FakeUtil {
  private static final Logger LOGGER = LoggerFactory.getLogger(FakeUtil.class);

  private FakeUtil() {}

  /**
   * Create path in provided <code>root</code> directory. Ignore existing files. Caller is
   * responsible for <code>root</code> directory setup (creation, permission).
   */
  public static void generate(IFichierPathStrategy strategy, Path root, Fichier fichier)
      throws IOException {
    String fichierPath = strategy.getPath(fichier);
    Path fullPath = root.resolve(fichierPath);
    FileUtils.forceMkdirParent(fullPath.toFile());
    if (!fullPath.toFile().createNewFile()) {
      LOGGER.debug("File {} already exists", fullPath);
    }
  }

  public static void process(
      EntityManagerHelper entityManagerHelper,
      IFichierPathStrategy strategy,
      Path root,
      int parallelism,
      int batchSize,
      List<Long> fichierIds) {
    ExecutorService executorService = Executors.newFixedThreadPool(parallelism);

    List<List<Long>> partitions = Lists.partition(fichierIds, batchSize);
    for (List<Long> partition : partitions) {
      executorService.submit(
          () -> FakeUtil.process(entityManagerHelper, root, strategy, partition));
    }

    try {
      executorService.shutdown();
      while (executorService.awaitTermination(5, TimeUnit.MINUTES) == false) {
        LOGGER.info("Processing Fichier...");
      }
    } catch (InterruptedException e) {
      LOGGER.error("Processing interrupted. Stopping...");
      executorService.shutdownNow();
      LOGGER.error("Processing interrupted. Stopped.");
      Thread.currentThread().interrupt();
    }
  }

  public static Integer process(
      EntityManagerHelper entityManagerHelper,
      Path root,
      IFichierPathStrategy strategy,
      List<Long> fichierIds) {
    int fichierCount = 0;
    try {
      List<Fichier> fichiers =
          entityManagerHelper.doWithTransaction(
              e -> {
                TypedQuery<Fichier> query =
                    e.createQuery("SELECT f FROM Fichier f WHERE f.id IN (:ids)", Fichier.class);
                query.setParameter("ids", fichierIds);
                return query.getResultList();
              });
      for (Fichier fichier : fichiers) {
        try {
          generate(strategy, root, fichier);
          fichierCount++;
        } catch (IOException e) {
          LOGGER.error("File {} cannot be created.", fichier, e);
        }
      }
      return fichierCount;
    } catch (Throwable e) {
      LOGGER.error("Error during batch processing.", e);
      return fichierCount;
    }
  }
}
