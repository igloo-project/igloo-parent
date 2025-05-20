package org.igloo.storage.tools.commands;

import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.Callable;
import org.igloo.storage.model.StorageUnit;
import org.igloo.storage.model.atomic.IStorageUnitType;
import org.igloo.storage.tools.ConfigurationException;
import org.igloo.storage.tools.StorageToolsMain;
import org.igloo.storage.tools.util.FichierUtil;
import org.igloo.storage.tools.util.FichierUtil.RunMode;
import org.igloo.storage.tools.util.StorageUnitTypeConverter;
import org.springframework.beans.factory.annotation.Autowired;
import picocli.CommandLine.Command;
import picocli.CommandLine.Mixin;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;
import picocli.CommandLine.ParentCommand;

@Command(
    name = "archive",
    header = "Archive to a target storage unit.",
    description =
        """
        Move a set of files (defined with --select-query-ids or --ids) from their
        current location to a new StorageUnit located at STORAGE_UNIT_PATH.

        STORAGE_UNIT_PATH must be located on the same disk than original data to
        ensure fast and safe move operations.

        StorageUnit is created STORAGE_UNIT_PATH is not found among existing
        StorageUnit.

        Path naming strategy is determined from origin and target storage unit.

        File from distinct storage unit may be moved to a same storage unit.

        Fichier instance is updated once file is successfully moved.

        A missing.txt file contains ids for all missing files (either at source
        or target location).

        A moved-not-updated.txt file contains ids for all files not updated
        in database whereas it is needed (because of a transaction issue).

        These files are generated in a temporary directory. User must clean
        these files if needed.

        In case there is some issue during process, rerun the command is mostly
        safe:
        * files already moved and updated will be considered as already moved,
        * if file is already moved and entity is still set to the former
          location, it will be updated to the new location.""")
public class ArchivingCommand implements Callable<Integer> {
  @ParentCommand StorageToolsMain parentCommand;

  @Option(names = {"--select-query-ids"})
  private Path selectQuery;

  @Option(names = {"--ids"})
  private List<Long> ids;

  @Option(
      names = {"--dry-run"},
      negatable = true)
  private boolean dryRun;

  @Mixin private ExecutorMixin executor = new ExecutorMixin();

  @Parameters(
      index = "0",
      paramLabel = "STORAGE_UNIT_PATH",
      description = "StorageUnit location. Path must be absolute.")
  private Path root;

  @Parameters(
      index = "1",
      paramLabel = "STORAGE_UNIT_TYPE",
      converter = StorageUnitTypeConverter.class,
      description = "StorateUnitType. Syntax is org.package.ClassName.ENUM_VALUE.")
  private IStorageUnitType storageUnitType;

  @Autowired private FichierUtil fichierUtil;

  @Override
  public Integer call() {
    parentCommand.prepare(this);

    if (selectQuery == null && (ids == null || ids.isEmpty())) {
      throw new ConfigurationException("--select-query-ids or --ids must be provided.");
    }
    if (selectQuery != null && (ids != null && !ids.isEmpty())) {
      throw new ConfigurationException("--select-query-ids and --ids cannot both be provided.");
    }

    StorageUnit storageUnit = fichierUtil.checkOrCreateStorageUnit(root, storageUnitType);

    // load ids
    List<Long> fichierIds = fichierUtil.loadFichierIdsFromQuery(selectQuery, ids);

    fichierUtil.processMove(
        executor, dryRun ? RunMode.DRY_RUN : RunMode.REAL, storageUnit, fichierIds);

    return 0;
  }
}
