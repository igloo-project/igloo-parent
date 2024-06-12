package test.core;

import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.PostgreSQLContainer;

//laisse la main à spring boot
public class PSQLTestContainerConfiguration {

	@Bean
	@ServiceConnection
	public PostgreSQLContainer<?> postgreSQLContainer() {
		return new PostgreSQLContainer<>("postgres:15.2-alpine")
			.withReuse(true);
	}
}
