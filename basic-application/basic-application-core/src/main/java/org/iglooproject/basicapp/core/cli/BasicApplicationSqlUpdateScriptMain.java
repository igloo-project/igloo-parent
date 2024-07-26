package org.iglooproject.basicapp.core.cli;

import java.util.Set;
import java.util.concurrent.Callable;
import org.igloo.hibernate.hbm.SqlOutput;
import org.igloo.hibernate.hbm.SqlUpdateScript;
import org.iglooproject.basicapp.core.config.spring.BasicApplicationCoreHeadlessConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

@Command(name = "basic-application-cli", mixinStandardHelpOptions = true)
public final class BasicApplicationSqlUpdateScriptMain extends AbstractBasicApplicationMain
    implements Callable<Integer> {

  private static final Logger LOGGER =
      LoggerFactory.getLogger(BasicApplicationSqlUpdateScriptMain.class);
  private static final Set<String> STDOUT_OPTIONS = Set.of("-", "stdout");

  @Parameters(index = "0", description = "Action", defaultValue = "update")
  private Action action;

  @Parameters(
      index = "1",
      description = "Output file. Use stdout or - to output to stdout",
      defaultValue = "stdout")
  private String outputFilename;

  private BasicApplicationSqlUpdateScriptMain() {}

  public static void main(String[] args) {
    CommandLine cl = new CommandLine(new BasicApplicationSqlUpdateScriptMain());
    System.exit(cl.execute(args));
  }

  @Override
  public Integer call() throws Exception {
    try (AnnotationConfigApplicationContext context =
        startContext("development", BasicApplicationCoreHeadlessConfig.class)) {
      SqlOutput output =
          STDOUT_OPTIONS.contains(outputFilename.toLowerCase())
              ? SqlOutput.stdout()
              : SqlOutput.file(outputFilename);
      SqlUpdateScript.writeSqlDiffScript(context, output, action.name());
      LOGGER.info("Initialization complete");
      return 0;
    } catch (
        Throwable e) { // NOSONAR We just want to log the Exception/Error, no error handling here.
      LOGGER.error("Error during initialization", e);
      return 1;
    }
  }

  public enum Action {
    create,
    update;
  }
}
