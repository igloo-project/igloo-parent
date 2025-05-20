package org.igloo.storage.tools.commands;

import java.util.concurrent.Callable;
import org.igloo.storage.tools.EntityManagerHelper;
import picocli.CommandLine.Command;
import picocli.CommandLine.ParentCommand;

@Command(name = "archivage")
public class ArchivageCommand implements Callable<Integer> {
  @ParentCommand EntityManagerHelper parentCommand;

  @Override
  public Integer call() {
    return 0;
  }
}
