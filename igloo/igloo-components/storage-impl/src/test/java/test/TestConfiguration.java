package test;

import igloo.test.listener.postgresql.PsqlTestContainerConfiguration;
import org.igloo.storage.model.Fichier;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.context.annotation.Import;

@SpringBootConfiguration
@EnableAutoConfiguration
@EntityScan(basePackageClasses = Fichier.class)
@Import(PsqlTestContainerConfiguration.class)
public class TestConfiguration {
  // spring boot configuration when test class is annotated with @SpringBootTest
}
