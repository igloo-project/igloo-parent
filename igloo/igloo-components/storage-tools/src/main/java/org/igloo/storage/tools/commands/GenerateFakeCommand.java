package org.igloo.storage.tools.commands;

import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.Callable;
import org.igloo.storage.tools.ConfigurationException;
import org.igloo.storage.tools.EntityManagerHelper;
import org.igloo.storage.tools.StorageToolsMain;
import org.igloo.storage.tools.util.FakeUtil;
import org.igloo.storage.tools.util.FichierUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import picocli.CommandLine.Command;
import picocli.CommandLine.Mixin;
import picocli.CommandLine.Option;
import picocli.CommandLine.ParentCommand;

@Command(
    name = "generate-fake",
    header = "Generate a fake folder tree from a storage database.",
    description =
        """
        Generate a file hierarchy from a storage database. Ids may be provided
        manually (--ids) or from an SQL query. Files are loaded from database
        so that path can be computed, then written to filesystem.

        --target-folder is the folder to store generated files. If missing,
        storageUnit setup is used.

        Existing files are never overwritten. Database is not modified.""")
public class GenerateFakeCommand implements Callable<Integer> {
  private static final Logger LOGGER = LoggerFactory.getLogger(GenerateFakeCommand.class);

  @ParentCommand StorageToolsMain parentCommand;

  @Option(names = {"--select-query-ids"})
  private Path selectQuery;

  @Option(names = {"--ids"})
  private List<Long> ids;

  @Option(names = {"--target-folder"})
  private Path root;

  @Mixin private ExecutorMixin executor = new ExecutorMixin();

  @Autowired private FichierUtil fichierUtil;

  @Autowired private EntityManagerHelper entityManagerHelper;

  @Override
  public Integer call() {
    parentCommand.prepare(this);

    if (selectQuery == null && (ids == null || ids.isEmpty())) {
      throw new ConfigurationException("--select-query-ids or --ids must be provided.");
    }
    if (selectQuery != null && (ids != null && !ids.isEmpty())) {
      throw new ConfigurationException("--select-query-ids and --ids cannot both be provided.");
    }

    // load ids
    List<Long> fichierIds = fichierUtil.loadFichierIdsFromQuery(selectQuery, ids);

    // create hierarchy from ids
    FakeUtil.process(
        entityManagerHelper, root, executor.parallelism, executor.batchSize, fichierIds);

    return 0;
  }
}
