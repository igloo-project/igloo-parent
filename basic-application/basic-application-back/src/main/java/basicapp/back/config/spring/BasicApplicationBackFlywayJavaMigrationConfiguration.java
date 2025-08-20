package basicapp.back.config.spring;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "db.migration.init")
public class BasicApplicationBackFlywayJavaMigrationConfiguration {}
