package org.igloo.storage.tools.util;

import com.google.common.base.Stopwatch;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import org.igloo.storage.model.Fichier;
import org.igloo.storage.tools.util.FichierUtil.MoveResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ArchivingProgressMonitor {

  private static final Logger LOGGER = LoggerFactory.getLogger(ArchivingProgressMonitor.class);

  /** File moved during the process or already at expected location. */
  private final AtomicInteger moved = new AtomicInteger();

  /** File is both missing at source and target location. */
  private final AtomicInteger missing = new AtomicInteger();

  /** Number of consistent updates. */
  private final AtomicInteger databaseConsistent = new AtomicInteger();

  /** Number of inconsistencies. There is a rollback and needed updates are not saved. */
  private final AtomicInteger databaseInconsistent = new AtomicInteger();

  /** File already at target locations. */
  private final AtomicInteger alreadyMoved = new AtomicInteger();

  /** File is present at source location, must be moved, but move failed. */
  private final AtomicInteger moveFailed = new AtomicInteger();

  /** To print id list of missing files (either at source or target location. */
  private final PrintStream missingStream;

  /** To pring id list of failed updates (see {@link #databaseInconsistent}) */
  private final PrintStream moveNotUpdatedStream;

  private final int total;

  private final Stopwatch stopwatch = Stopwatch.createStarted();

  private final Path tempDirectory;

  public ArchivingProgressMonitor(int total) throws IOException {
    this.total = total;
    tempDirectory = Files.createTempDirectory("archive-").toAbsolutePath();
    missingStream =
        new PrintStream(new FileOutputStream(tempDirectory.resolve("missings.txt").toFile()), true);
    moveNotUpdatedStream =
        new PrintStream(
            new FileOutputStream(tempDirectory.resolve("moved-not-updated.txt").toFile()), true);
  }

  public Path getTempDirectory() {
    return tempDirectory;
  }

  public void moved() {
    moved.incrementAndGet();
  }

  public void moveFailed() {
    moveFailed.incrementAndGet();
  }

  public void missing(Fichier fichier) {
    missingStream.println(fichier.getId());
    missing.incrementAndGet();
    moved.incrementAndGet();
  }

  public void alreadyMoved() {
    alreadyMoved.incrementAndGet();
    moved.incrementAndGet();
  }

  /** Register inconsistencies if update failed. */
  public void databaseFailed(Map<Long, MoveResult> results) {
    for (Map.Entry<Long, MoveResult> item : results.entrySet()) {
      switch (item.getValue()) {
        case MOVED, ALREADY_MOVED, MISSING:
          // file location is not the location from database
          // or as file is missing, we want database to be updated
          moveNotUpdatedStream.println(item.getKey());
          databaseInconsistent.incrementAndGet();
          break;
        case MOVE_FAILED, UNTOUCHED:
          // entity does not need an update as no operation is done either as move failed or
          // database is already set to new location (file may be missing, but entity is switched).
          databaseConsistent.incrementAndGet();
          // database not updated as move failed
          break;
      }
    }
  }

  /** Register success if update succeeded. */
  public void databaseSuccess(Map<Long, MoveResult> results) {
    databaseConsistent.addAndGet(results.keySet().size());
  }

  /** Print current status. */
  public void printShortStatus() {
    int progress;
    if (total == 0) {
      progress = 100;
    } else {
      progress = (moved.get() + moveFailed.get() + missing.get()) * 100 / total;
    }
    LOGGER.info("Elapsed time: {}% ({} s.)", progress, stopwatch.elapsed(TimeUnit.SECONDS));
    LOGGER.info(
        "Archiving: moved: {} - move-failed: {} - consistent: {} - inconsistent: {} / total: {}",
        moved.get(),
        moveFailed.get(),
        databaseConsistent.get(),
        databaseInconsistent.get(),
        total);
    LOGGER.info("Archiving: missing: {} - alreay-moved: {}", missing.get(), alreadyMoved.get());
  }

  /** Print current status. */
  public void printStatus() {
    printShortStatus();
    if (missing.get() != 0) {
      LOGGER.warn(
          "There is {} files missing. Some checks may be needed in missing.txt, "
              + "but it should not be an archiving process issue.");
    }
    if (databaseInconsistent.get() == 0 && moveFailed.get() == 0) {
      LOGGER.info("Archiving is successfull.");
      if (moved.get() + missing.get() != total) {
        LOGGER.warn("But there is an issue as moved + missing != total");
      }
      if (databaseConsistent.get() != total) {
        LOGGER.warn("But there is an issue as databaseConsistent != total");
      }
    } else {
      if (databaseInconsistent.get() != 0) {
        LOGGER.error(
            "{} files were not updated accordingly to their new location. Check the database "
                + "issue and rerun the script to perform database update (cases may be treated "
                + "as already moved files.",
            databaseInconsistent.get());
      }
      if (moveFailed.get() != 0) {
        LOGGER.error(
            "{} files cannot be moved. Database is still consistent. Fix the move issue "
                + "and rerun the script.",
            moveFailed.get());
      }
    }
  }
}
