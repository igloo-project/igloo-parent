package igloo.test.listener.postgresql;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.PostgreSQLContainer;

public class PsqlTestContainerConfiguration {

  @Value("${testContainer.database.name}")
  String databaseName;

  @Value("${testContainer.database.userName}")
  String username;

  @Value("${testContainer.database.password}")
  String password;

  @Value("${testContainer.database.exposedPorts}")
  String exposedPorts;

  @Value("${testContainer.database.dockerImageName}")
  String dockerImageName;

  @Bean
  @ServiceConnection
  public PostgreSQLContainer<?> postgreSQLContainer() { // NOSONAR
    try (PostgreSQLContainer<?> container = new PostgreSQLContainer<>(dockerImageName)) {
      return container
          .withDatabaseName(databaseName)
          .withUsername(username)
          .withPassword(password)
          .withExposedPorts(Integer.parseInt(exposedPorts))
          .withReuse(true);
    }
  }
}
