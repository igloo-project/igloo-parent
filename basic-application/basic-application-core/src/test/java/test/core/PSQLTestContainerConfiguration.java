package test.core;

import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.PostgreSQLContainer;

//laisse la main à spring boot
public class PSQLTestContainerConfiguration {

	// TODO faire en sorte de pouvoir fixer le port ou retrouver le port facilement
	// avec docker ps 
	// voir preserve container si exception ??
	// ajouter les configs dans fichier .properties 
	@Bean
	@ServiceConnection
	public PostgreSQLContainer<?> postgreSQLContainer() {
		PostgreSQLContainer self = new PostgreSQLContainer<>("postgres:15-alpine")
			.withDatabaseName("basic_application_test")
			.withUsername("basic_application_test")
			.withPassword("basic_application_test")
			.withExposedPorts(5432)
			.withReuse(true);
		return self;
	}
}
