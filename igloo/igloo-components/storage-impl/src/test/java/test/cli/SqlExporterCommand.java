package test.cli;

import java.util.concurrent.Callable;
import org.iglooproject.jpa.sql.BaseSqlExporterCommand;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import picocli.CommandLine;
import test.TestConfiguration;

@Configuration
@Import(TestConfiguration.class) // entity/model scanning
public class SqlExporterCommand extends BaseSqlExporterCommand implements Callable<Integer> {

  public static void main(String[] args) {
    CommandLine cl = new CommandLine(new SqlExporterCommand());
    System.exit(cl.execute(args));
  }
}
