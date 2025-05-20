package org.igloo.storage.tools.commands;

import picocli.CommandLine.Option;

public class ExecutorMixin {

  @Option(
      names = "--executor-batch-size",
      defaultValue = "1000",
      description = "Number of item by executor batch.")
  public Integer batchSize = 1_000;

  @Option(
      names = "--executor-parallelism",
      defaultValue = "10",
      description = "Number of threads for executor.")
  public Integer parallelism = 10;

  @Option(
      names = "--executor-print-delay",
      defaultValue = "60",
      description = "Delay in second between status updates.")
  public Integer printDelayInSeconds = 10;
}
