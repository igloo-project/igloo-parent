package test.core;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.PostgreSQLContainer;

//laisse la main à spring boot
public class PSQLTestContainerConfiguration {

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
	
	// TODO faire en sorte de pouvoir fixer le port ou retrouver le port facilement
	// avec docker ps
	@Bean
	@ServiceConnection
	public PostgreSQLContainer<?> postgreSQLContainer() {
		return new PostgreSQLContainer<>(dockerImageName)
			.withDatabaseName(databaseName)
			.withUsername(username)
			.withPassword(password)
			.withExposedPorts(Integer.parseInt(exposedPorts))
			.withReuse(true);
	}
}
