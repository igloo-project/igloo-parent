package basicapp.back.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "db.migration.init")
public class BasicApplicationBackFlywayJavaMigrationConfiguration {}
