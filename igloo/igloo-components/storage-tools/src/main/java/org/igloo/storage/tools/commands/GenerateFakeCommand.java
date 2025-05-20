package org.igloo.storage.tools.commands;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import org.igloo.storage.impl.FichierPathStrategy;
import org.igloo.storage.impl.LegacyFichierPathStrategy;
import org.igloo.storage.model.atomic.IFichierPathStrategy;
import org.igloo.storage.tools.ConfigurationException;
import org.igloo.storage.tools.Main;
import org.igloo.storage.tools.fake.FakeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import picocli.CommandLine.Command;
import picocli.CommandLine.ITypeConverter;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;
import picocli.CommandLine.ParentCommand;
import picocli.CommandLine.TypeConversionException;

@Command(name = "generate-fake")
public class GenerateFakeCommand implements Callable<Integer> {
  private static final Logger LOGGER = LoggerFactory.getLogger(GenerateFakeCommand.class);

  @ParentCommand Main parentCommand;

  @Option(names = {"--select-query-ids"})
  private Path selectQuery;

  @Option(names = {"--ids"})
  private List<Long> ids;

  @Option(
      names = {"--path-strategy"},
      converter = PathStrategyConverter.class,
      defaultValue = "FichierPathStrategy")
  private IFichierPathStrategy pathStrategy;

  @Parameters(index = "0")
  private Path root;

  private int batchSize = 1_000;

  private int parallelism = 10;

  @Override
  public Integer call() {
    parentCommand.prepare();

    if (selectQuery == null && (ids == null || ids.isEmpty())) {
      throw new ConfigurationException("--select-query-ids or --ids must be provided.");
    }
    if (selectQuery != null && (ids != null && !ids.isEmpty())) {
      throw new ConfigurationException("--select-query-ids and --ids cannot both be provided.");
    }

    List<Long> fichierIds = new ArrayList<>();
    if (ids != null && !ids.isEmpty()) {
      fichierIds = ids;
    } else {
      try {
        String query = Files.readString(selectQuery, StandardCharsets.UTF_8);
        fichierIds =
            parentCommand.doWithTransaction(
                e -> {
                  @SuppressWarnings("unchecked")
                  List<Long> result = e.createNativeQuery(query, Long.class).getResultList();
                  return result;
                });
      } catch (IOException e) {
        throw new ConfigurationException(
            "Query cannot be extracted from %s".formatted(selectQuery), e);
      } catch (DataAccessException e) {
        throw new ConfigurationException(
            "Ids cannot be loaded from query loaded from file %s".formatted(selectQuery), e);
      }
    }
    LinkedHashSet<Long> fichierIdsSet = new LinkedHashSet<>();
    fichierIdsSet.addAll(fichierIds);

    LOGGER.info("{} distinct ids loaded (from {} ids)", fichierIdsSet.size(), fichierIds.size());

    // deduplicate and keep order
    fichierIds.clear();
    fichierIds.addAll(fichierIdsSet);

    FakeUtil.process(parentCommand, pathStrategy, root, parallelism, batchSize, fichierIds);

    return 0;
  }

  public static class PathStrategyConverter implements ITypeConverter<IFichierPathStrategy> {
    private Map<String, IFichierPathStrategy> strategies =
        Map.<String, IFichierPathStrategy>of(
            FichierPathStrategy.class.getSimpleName(), new FichierPathStrategy(1),
            LegacyFichierPathStrategy.class.getSimpleName(), new LegacyFichierPathStrategy(1));

    @Override
    public IFichierPathStrategy convert(String value) throws Exception {
      IFichierPathStrategy strategy = strategies.get(value);
      if (strategy == null) {
        throw new TypeConversionException("Unkown strategy %s".formatted(value));
      }
      return strategy;
    }
  }
}
