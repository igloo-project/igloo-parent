package org.iglooproject.sass.cli;

import com.google.common.base.Stopwatch;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import org.iglooproject.sass.config.ISassConfigurationProvider;
import org.iglooproject.sass.model.ScssStylesheetInformation;
import org.iglooproject.sass.service.ScssServiceImpl;
import org.iglooproject.sass.service.StaticResourceHelper;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

@Command(
    name = "igloo-scss",
    description = "Scss files generation tool.",
    mixinStandardHelpOptions = true)
public class ScssMain implements Callable<Integer> {

  @Option(
      names = "--generation-path",
      description = "Generated files are created in GENERATION_PATH/RESOURCE_PATH/checksum.css",
      paramLabel = "GENERATION_PATH")
  private String generatedSourcesPath;

  @Option(
      names = "--scopes",
      description = "Scope paths to consider: format scopeName:package.className",
      paramLabel = "SCOPE")
  private List<String> scopes = new ArrayList<>();

  @Option(
      names = "--resource-path",
      description =
          "Classpath path where generated files are put (filenames are hash computed from source name)",
      defaultValue = StaticResourceHelper.DEFAULT_STATIC_SCSS_RESOURCE_PATH,
      paramLabel = "RESOURCE_PATH")
  private String resourcePath;

  @Option(names = "--quiet", description = "Only print error feedback", defaultValue = "false")
  private boolean quiet;

  @Parameters(description = "Scss files to process, format: package.ClassName:filename.scss")
  private List<String> stylesheets;

  public static void main(String[] args) {
    CommandLine cl = new CommandLine(new ScssMain());
    cl.execute(args);
  }

  @Override
  public Integer call() {
    try {
      Stopwatch sw = Stopwatch.createStarted();
      ISassConfigurationProvider configuration =
          new ISassConfigurationProvider() {
            @Override
            public boolean isAutoprefixerEnabled() {
              return true;
            }
          };
      ScssServiceImpl scssService = new ScssServiceImpl(configuration);
      for (String scope : scopes) {
        try {
          registerScope(scssService, scope);
        } catch (RuntimeException | ClassNotFoundException scopeException) {
          scopeException.printStackTrace(System.err); // NOSONAR
          System.err.println(
              String.format("Failure to register scope %s. Generation failure.", scope)); // NOSONAR
          return -1;
        }
      }
      for (String scss : stylesheets) {
        processScss(scssService, scss);
      }
      if (!quiet) {
        System.out.println(
            String.format(
                "Scss generation time: %d ms.", sw.elapsed(TimeUnit.MILLISECONDS))); // NOSONAR
      }
      return 0;
    } catch (RuntimeException e) {
      e.printStackTrace(System.err); // NOSONAR
      System.err.println(
          "One or more scss files cannot be generated. Generation failure."); // NOSONAR
      return 1;
    }
  }

  private void registerScope(ScssServiceImpl scssService, String scope)
      throws ClassNotFoundException {
    String scopeName = scope.split(":")[0];
    String scopeClass = scope.split(":")[1];
    scssService.registerImportScope(scopeName, Class.forName(scopeClass));
  }

  private void processScss(ScssServiceImpl scssService, String scss) {
    try {
      Stopwatch swStylesheet = Stopwatch.createStarted();
      String resourceReferenceClassName = scss.split(":")[0];
      String filename = scss.split(":")[1];
      Class<?> resourceReferenceClass = Class.forName(resourceReferenceClassName);
      ScssStylesheetInformation information =
          scssService.getCompiledStylesheet(resourceReferenceClass, filename);
      Path outputFilename =
          StaticResourceHelper.getStaticResourcePath(
              resourcePath, resourceReferenceClass, filename);
      Path cssPath =
          Path.of(Optional.ofNullable(generatedSourcesPath).orElse(System.getProperty("user.dir")))
              .resolve(outputFilename);
      cssPath.getParent().toFile().mkdirs();
      String header = String.format("/* Generated from %s on %s */%n%n", scss, Instant.now());
      Files.writeString(cssPath, header + information.getSource());
      if (!quiet) {
        System.out.println(
            String.format(
                "Scss processing: %s -> %s (%d ms.)",
                scss, cssPath, swStylesheet.elapsed(TimeUnit.MILLISECONDS))); // NOSONAR
      }
    } catch (RuntimeException | IOException | ClassNotFoundException e) {
      throw new IllegalStateException(String.format("Error generating %s", scss), e);
    }
  }
}
