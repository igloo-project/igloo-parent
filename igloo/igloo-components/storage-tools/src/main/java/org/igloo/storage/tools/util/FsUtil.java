package org.igloo.storage.tools.util;

import java.io.IOException;
import java.nio.file.Path;
import org.apache.commons.io.FileUtils;
import org.igloo.storage.model.Fichier;
import org.igloo.storage.model.StorageUnit;
import org.igloo.storage.model.atomic.IFichierPathStrategy;
import org.igloo.storage.tools.util.FichierUtil.MoveResult;
import org.igloo.storage.tools.util.FichierUtil.RunMode;

public class FsUtil {

  /**
   * Move <code>fichier</code> and provides {@link MoveResult} accordingly. <code>
   * monitor</code> is called for interactive feedback.
   *
   * @see MoveResult
   */
  public MoveResult move(
      ArchivingProgressMonitor monitor,
      RunMode runMode,
      StorageUnit targetStorageUnit,
      Fichier fichier) {
    StorageUnit sourceStorageUnit = fichier.getStorageUnit();
    Path source = Path.of(sourceStorageUnit.getPath(), fichier.getRelativePath());
    IFichierPathStrategy targetPathStrategy = targetStorageUnit.getType().getFichierPathStrategy();
    Path target = Path.of(targetStorageUnit.getPath(), targetPathStrategy.getPath(fichier));
    // item is already archived (file may be missing !)
    if (fichier.getStorageUnit().equals(targetStorageUnit)) {
      if (source.toFile().exists()) {
        monitor.alreadyMoved();
      } else {
        monitor.missing(fichier);
      }
      return MoveResult.UNTOUCHED;
    }
    // file is already at target location, database need an update
    if (target.toFile().exists()) {
      monitor.alreadyMoved();
      return MoveResult.ALREADY_MOVED;
    }
    // source file is missing
    if (!source.toFile().exists()) {
      monitor.missing(fichier);
      return MoveResult.MISSING;
    }
    // file must be moved
    try {
      if (RunMode.REAL.equals(runMode)) {
        FileUtils.forceMkdirParent(target.getParent().toFile());
        FileUtils.moveFile(source.toFile(), target.toFile());
      }
      monitor.moved();
      return MoveResult.MOVED;
    } catch (IOException | RuntimeException e) {
      monitor.moveFailed();
      return MoveResult.MOVE_FAILED;
    }
  }
}
