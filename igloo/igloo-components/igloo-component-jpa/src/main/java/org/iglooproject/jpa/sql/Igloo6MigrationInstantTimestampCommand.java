package org.iglooproject.jpa.sql;

import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.Callable;
import org.iglooproject.jpa.autoconfigure.SqlExporter;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

@EnableAutoConfiguration(
    excludeName = {
      "org.iglooproject.jpa.more.autoconfigure.JpaMoreAutoConfiguration",
      "org.iglooproject.jpa.security.autoconfigure.SecurityAutoConfiguration"
    })
@Command(
    name = "igloo6-migration-instant",
    mixinStandardHelpOptions = true,
    description = "Create a migration script for Igloo 6 and Instant fields")
public class Igloo6MigrationInstantTimestampCommand implements Callable<Integer> {

  @Parameters(
      index = "0",
      description = "Output file. Use stdout or - to output to stdout",
      defaultValue = "stdout")
  private String target;

  public Igloo6MigrationInstantTimestampCommand() {
    super();
  }

  @Override
  public Integer call() throws Exception {
    Path output = List.of("-", "stdout").contains(target) ? null : Path.of(target);
    SqlExporter.igloo6InstantTimestamp(new SpringApplicationBuilder(this.getClass()), output);
    return 0;
  }
}
