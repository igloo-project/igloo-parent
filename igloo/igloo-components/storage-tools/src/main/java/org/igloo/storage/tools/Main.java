package org.igloo.storage.tools;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Properties;
import java.util.function.Function;
import org.igloo.storage.model.Fichier;
import org.igloo.storage.model.StorageUnit;
import org.igloo.storage.tools.commands.ArchivageCommand;
import org.igloo.storage.tools.commands.GenerateFakeCommand;
import org.iglooproject.jpa.util.EntityManagerUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.interceptor.DefaultTransactionAttribute;
import org.springframework.transaction.interceptor.TransactionAttribute;
import org.springframework.transaction.support.TransactionTemplate;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(
    mixinStandardHelpOptions = true,
    subcommands = {ArchivageCommand.class, GenerateFakeCommand.class})
@Configuration
@EnableAutoConfiguration
@EntityScan(basePackageClasses = {Fichier.class, StorageUnit.class})
public class Main implements EntityManagerHelper {

  private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

  private ConfigurableApplicationContext context;

  @Option(
      names = {"-c", "--config"},
      required = true)
  private Path configFile;

  public static void main(String[] args) {
    Main main = new Main();
    try {
      int rc = new CommandLine(main).execute(args);
      System.exit(rc);
    } finally {
      main.cleanup();
    }
  }

  public void cleanup() {
    if (context != null) {
      context.stop();
    }
  }

  public void prepare() {
    SpringApplication springApplication =
        new SpringApplication(Main.class) {
          @Override
          protected void configureEnvironment(ConfigurableEnvironment environment, String[] args) {
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
    context = springApplication.run();
    Objects.requireNonNull(context.getBeanProvider(EntityManagerFactory.class).getIfAvailable());
    Objects.requireNonNull(
        context.getBeanProvider(PlatformTransactionManager.class).getIfAvailable());
  }

  private TransactionTemplate readOnlyTransactionTemplate() {
    DefaultTransactionAttribute readOnlyTransactionAttribute =
        new DefaultTransactionAttribute(TransactionAttribute.PROPAGATION_REQUIRES_NEW);
    readOnlyTransactionAttribute.setReadOnly(true);
    return new TransactionTemplate(
        context.getBean(PlatformTransactionManager.class), readOnlyTransactionAttribute);
  }

  @Override
  public <T> T doWithTransaction(Function<EntityManager, T> consumer) {
    return readOnlyTransactionTemplate()
        .execute(
            t -> {
              EntityManager entityManager =
                  context.getBean(EntityManagerUtils.class).getCurrentEntityManager();
              return consumer.apply(entityManager);
            });
  }

  public class ConfigurationException extends RuntimeException {
    public ConfigurationException(String message, Throwable cause) {
      super(message, cause);
    }

    private static final long serialVersionUID = 1L;
  }
}
