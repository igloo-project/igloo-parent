package org.igloo.storage.tools.util;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FakeUtil {
  private static final Logger LOGGER = LoggerFactory.getLogger(FakeUtil.class);

  private FakeUtil() {}

  /** Split work in partitions. */
  public static void process(
      EntityManagerHelper entityManagerHelper,
      Path root,
      int parallelism,
      int batchSize,
      List<Long> fichierIds) {
    ExecutorService executorService = Executors.newFixedThreadPool(parallelism);

    List<List<Long>> partitions = Lists.partition(fichierIds, batchSize);
    for (List<Long> partition : partitions) {
      executorService.submit(() -> FakeUtil.process(entityManagerHelper, root, partition));
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

  /**
   * Create path in provided <code>root</code> directory. Ignore existing files. Caller is
   * responsible for <code>root</code> directory setup (creation, permission).
   */
  protected static void generate(Path root, Fichier fichier) throws IOException {
    String relativePath =
        fichier.getStorageUnit().getType().getFichierPathStrategy().getPath(fichier);
    Path fullPath;
    if (root == null) {
      fullPath = Path.of(fichier.getStorageUnit().getPath(), relativePath);
    } else {
      fullPath = root.resolve(relativePath);
    }
    FileUtils.forceMkdirParent(fullPath.toFile());
    if (!fullPath.toFile().createNewFile()) {
      LOGGER.debug("File {} already exists", fullPath);
    }
    LOGGER.debug("File {} created", fullPath);
  }

  /** Process a work partition. */
  protected static Integer process(
      EntityManagerHelper entityManagerHelper, Path root, List<Long> fichierIds) {
    int fichierCount = 0;
    try {
      List<Fichier> fichiers =
          entityManagerHelper.doWithReadOnlyTransaction(
              e -> {
                TypedQuery<Fichier> query =
                    e.createQuery("SELECT f FROM Fichier f WHERE f.id IN (:ids)", Fichier.class);
                query.setParameter("ids", fichierIds);
                return query.getResultList();
              });
      for (Fichier fichier : fichiers) {
        try {
          generate(root, fichier);
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
