package org.igloo.storage.tools;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Stopwatch;
import igloo.jpa.batch.autoconfigure.JpaBatchAutoConfiguration;
import jakarta.persistence.EntityManagerFactory;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Properties;
import org.igloo.storage.impl.DatabaseOperations;
import org.igloo.storage.model.Fichier;
import org.igloo.storage.model.StorageUnit;
import org.igloo.storage.tools.commands.ArchivingCommand;
import org.igloo.storage.tools.commands.GenerateFakeCommand;
import org.igloo.storage.tools.util.EntityManagerHelper;
import org.igloo.storage.tools.util.FichierUtil;
import org.igloo.storage.tools.util.FsUtil;
import org.iglooproject.jpa.more.autoconfigure.JpaMoreModelAutoConfiguration;
import org.iglooproject.jpa.security.autoconfigure.SecurityModelAutoConfiguration;
import org.iglooproject.jpa.util.EntityManagerUtils;
import org.iglooproject.spring.autoconfigure.SecurityPropertyRegistryAutoConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.cache.CacheAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.freemarker.FreeMarkerAutoConfiguration;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.task.TaskExecutionAutoConfiguration;
import org.springframework.boot.autoconfigure.task.TaskSchedulingAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.transaction.PlatformTransactionManager;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(
    mixinStandardHelpOptions = true,
    subcommands = {ArchivingCommand.class, GenerateFakeCommand.class})
@Configuration
@EnableAutoConfiguration(
    exclude = {
      SecurityAutoConfiguration.class,
      org.iglooproject.jpa.security.autoconfigure.SecurityAutoConfiguration.class,
      JpaBatchAutoConfiguration.class,
      JpaMoreModelAutoConfiguration.class,
      SecurityPropertyRegistryAutoConfiguration.class,
      SecurityModelAutoConfiguration.class,
      FreeMarkerAutoConfiguration.class,
      JacksonAutoConfiguration.class,
      CacheAutoConfiguration.class,
      TaskExecutionAutoConfiguration.class,
      TaskSchedulingAutoConfiguration.class
    })
@EntityScan(basePackageClasses = {Fichier.class, StorageUnit.class})
public class StorageToolsMain {

  static {
    // we need to override logger config as application jar provide a log4j2.properties
    System.setProperty("log4j2.configurationFile", "log4j2-storage-tools.properties");
  }

  private static final Logger LOGGER = LoggerFactory.getLogger(StorageToolsMain.class);

  /**
   * If set, context is managed by test. Do not init context locally, do not close context, do not
   * {@link System#exit(int)}.
   */
  @VisibleForTesting public static ConfigurableApplicationContext APPLICATION_CONTEXT;

  private ConfigurableApplicationContext context;

  @Option(
      names = {"-c", "--config"},
      required = true)
  private Path configFile;

  public static void main(String[] args) {
    Stopwatch sw = Stopwatch.createStarted();
    StorageToolsMain main = new StorageToolsMain();
    int rc = -1;
    try {
      rc = new CommandLine(main).execute(args);
    } finally {
      main.cleanup();
      LOGGER.info("Elapsed time: {} s.", .001 * sw.elapsed().toMillis());
    }
    if (APPLICATION_CONTEXT == null) {
      System.exit(rc);
    }
  }

  public void cleanup() {
    if (APPLICATION_CONTEXT == null && context != null) {
      context.stop();
    }
  }

  /** This method must be called from picocli subcommand to setup spring context. */
  public void prepare(Object command) {
    LOGGER.debug("Initializing spring...");

    if (APPLICATION_CONTEXT == null) {
      // load configuration for command line argument.
      SpringApplication springApplication =
          new SpringApplication(StorageToolsMain.class) {
            @Override
            protected void configureEnvironment(
                ConfigurableEnvironment environment, String[] args) {
              try {
                super.configureEnvironment(environment, args);
                Properties properties = new Properties();
                try (InputStream is = new FileInputStream(configFile.toFile())) {
                  properties.load(is);
                }
                environment
                    .getPropertySources()
                    .addLast(new PropertiesPropertySource("cli", properties));
              } catch (IOException e) {
                throw new ConfigurationException(
                    "Error loading configuration propertiers %s".formatted(configFile), e);
              }
            }
          };

      // start Spring context
      context = springApplication.run();
    } else {
      context = APPLICATION_CONTEXT;
    }
    // inject @Autowired into picocli command
    context.getAutowireCapableBeanFactory().autowireBean(command);
    Objects.requireNonNull(context.getBeanProvider(EntityManagerFactory.class).getIfAvailable());
    Objects.requireNonNull(
        context.getBeanProvider(PlatformTransactionManager.class).getIfAvailable());
    LOGGER.debug("Spring initialized.");
  }

  @Bean
  public EntityManagerHelper entityManagerHelper(
      EntityManagerUtils entityManagerUtils,
      PlatformTransactionManager platformTransactionManager,
      DatabaseOperations databaseOperations) {
    return new EntityManagerHelper(
        entityManagerUtils, platformTransactionManager, databaseOperations);
  }

  @Bean
  public FichierUtil fichierUtil(EntityManagerHelper entityManagerHelper) {
    return new FichierUtil(entityManagerHelper, new FsUtil());
  }
}
