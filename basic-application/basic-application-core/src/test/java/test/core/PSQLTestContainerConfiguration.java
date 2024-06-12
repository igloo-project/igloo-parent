package test.core;

import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.PostgreSQLContainer;

//laisse la main à spring boot
public class PSQLTestContainerConfiguration {

	@Bean
	@ServiceConnection
	public PostgreSQLContainer<?> postgreSQLContainer() {
		PostgreSQLContainer self = new PostgreSQLContainer<>("postgres:15.2-alpine")
			.withDatabaseName("basic_application_test")
			.withUsername("basic_application_test")
			.withPassword("basic_application_test")
			.withExposedPorts(5432)
			.withReuse(true);
		return self;
	}
}
