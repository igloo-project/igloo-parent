package org.iglooproject.jpa.sql;

import com.google.common.io.CharStreams;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import jakarta.persistence.EntityManagerFactory;
import java.io.FileNotFoundException;
import java.io.FilterWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.file.Path;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.tool.schema.SourceType;
import org.hibernate.tool.schema.TargetType;
import org.hibernate.tool.schema.internal.DefaultSchemaFilter;
import org.hibernate.tool.schema.internal.ExceptionHandlerCollectingImpl;
import org.hibernate.tool.schema.internal.exec.ScriptTargetOutputToWriter;
import org.hibernate.tool.schema.spi.ContributableMatcher;
import org.hibernate.tool.schema.spi.ExceptionHandler;
import org.hibernate.tool.schema.spi.ExecutionOptions;
import org.hibernate.tool.schema.spi.SchemaFilter;
import org.hibernate.tool.schema.spi.SchemaManagementTool;
import org.hibernate.tool.schema.spi.ScriptSourceInput;
import org.hibernate.tool.schema.spi.ScriptTargetOutput;
import org.hibernate.tool.schema.spi.SourceDescriptor;
import org.hibernate.tool.schema.spi.TargetDescriptor;
import org.igloo.hibernate.hbm.MetadataRegistryIntegrator;
import org.springframework.util.StringUtils;

public class SqlRunner {
  private final EntityManagerFactory entityManagerFactory;

  private final MetadataRegistryIntegrator metadataRegistryIntegrator;

  public SqlRunner(
      EntityManagerFactory entityManagerFactory,
      MetadataRegistryIntegrator metadataRegistryIntegrator) {
    this.entityManagerFactory = entityManagerFactory;
    this.metadataRegistryIntegrator = metadataRegistryIntegrator;
  }

  /**
   * Write SQL migration script in expected output; stdout if null, else targeted file.
   *
   * @param action <code>update</code> or <code>create</code>.
   * @param output target for output, stdout if null, else targeted file.
   */
  public void migrationScript(@Nonnull SqlRunner.Action action, @Nullable Path output) {
    var serviceRegistry =
        entityManagerFactory.unwrap(SessionFactoryImplementor.class).getServiceRegistry();
    var schemaTool = serviceRegistry.getService(SchemaManagementTool.class);
    StringWriter writer = new StringWriter();
    generateSql(action, schemaTool, writer);
    writeResult(output, writer.toString());
  }

  private void generateSql(
      SqlRunner.Action action, SchemaManagementTool schemaTool, Writer writer) {
    ExceptionHandlerCollectingImpl exceptionHandler = new ExceptionHandlerCollectingImpl();
    TargetDescriptor targetDescriptor = new TargetDescriptorImpl(writer);
    ExecutionOptions executionOptions =
        new ExecutionOptions() {
          @Override
          public boolean shouldManageNamespaces() {
            return false;
          }

          @Override
          public Map<String, Object> getConfigurationValues() {
            Map<String, Object> config = new HashMap<>();
            config.put(AvailableSettings.HBM2DDL_DELIMITER, ";");
            return config;
          }

          @Override
          public ExceptionHandler getExceptionHandler() {
            return exceptionHandler;
          }

          @Override
          public SchemaFilter getSchemaFilter() {
            return DefaultSchemaFilter.INSTANCE;
          }
        };
    if (Action.create.equals(action)) {
      final SourceDescriptor sourceDescriptor =
          new SourceDescriptor() {
            @Override
            public SourceType getSourceType() {
              return SourceType.METADATA;
            }

            @Override
            public ScriptSourceInput getScriptSourceInput() {
              return null;
            }
          };
      schemaTool
          .getSchemaCreator(new HashMap<>())
          .doCreation(
              metadataRegistryIntegrator.getMetadata(),
              executionOptions,
              ContributableMatcher.ALL,
              sourceDescriptor,
              targetDescriptor);
    } else if (Action.update.equals(action)) {
      schemaTool
          .getSchemaMigrator(new HashMap<>())
          .doMigration(
              metadataRegistryIntegrator.getMetadata(),
              executionOptions,
              ContributableMatcher.ALL,
              targetDescriptor);
    }
    if (!exceptionHandler.getExceptions().isEmpty()) {
      var e = new RuntimeException("SQL extraction thows one or more exceptions.");
      exceptionHandler.getExceptions().stream().forEach(e::addSuppressed);
      throw e; // NOSONAR
    }
  }

  private void writeResult(Path output, String content) {
    try (PrintWriter pw = writer(output)) { // NOSONAR
      if (StringUtils.hasLength(content)) {
        pw.println("-- Hibernate hbm2ddl script");
        pw.println(content);
        pw.println("-- Hibernate hbm2ddl script (end)");
      } else {
        pw.println("-- Hibernate hbm2ddl script is empty");
      }
    } catch (IOException e) {
      throw new RuntimeException(String.format("Error writing SQL to %s", output)); // NOSONAR
    }
  }

  /**
   * Returns an output {@link PrintWriter} targetting provided not-null <code>output</code> file,
   * else stdout.
   *
   * @param output desired output file, null is output must be printed on stdout.
   * @return a {@link PrintWriter} targetting the expected file or stdout.
   * @throws FileNotFoundException
   */
  private PrintWriter writer(@Nullable Path output) throws FileNotFoundException {
    if (output == null) {
      return new PrintWriter(
          new FilterWriter(
              CharStreams.asWriter(System.out)) { // NOSONAR, we want to output to stdout
            @Override
            public void close() throws IOException {
              // prevent stdout close
            }
          });
    } else {
      return new PrintWriter(output.toFile());
    }
  }

  public enum Action { // SONAR
    /** Creation script (from empty database). */
    create, // NOSONAR mapping is simpler if lowercased
    /** Update script (from an existing database). */
    update; // NOSONAR mapping is simpler if lowercased
  }

  /** Target descriptor to write result to a provided {@link Writer}. */
  private static class TargetDescriptorImpl implements TargetDescriptor {
    private final ScriptTargetOutputToWriter targetOutput;
    private final EnumSet<TargetType> types = EnumSet.of(TargetType.SCRIPT);

    public TargetDescriptorImpl(Writer writer) {
      targetOutput = new ScriptTargetOutputToWriter(writer);
    }

    @Override
    public EnumSet<TargetType> getTargetTypes() {
      return types;
    }

    @Override
    public ScriptTargetOutput getScriptTargetOutput() {
      return targetOutput;
    }
  }
}
