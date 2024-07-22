package org.iglooproject.jpa.autoconfigure;

import java.nio.file.Path;
import org.iglooproject.config.bootstrap.spring.ExtendedApplicationContextInitializer;
import org.iglooproject.jpa.sql.SqlRunner;
import org.iglooproject.jpa.sql.SqlRunner.Action;
import org.springframework.boot.Banner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;

public class SqlExporter {

  private SqlExporter() {}

  /** Initialize a context for SQL runner. */
  public static void export(SpringApplicationBuilder builder, Action action, Path output) {
    var context =
        builder
            .initializers(new ExtendedApplicationContextInitializer())
            .web(WebApplicationType.NONE)
            .bannerMode(Banner.Mode.OFF)
            .properties(
                "spring.jpa.properties.hibernate.search.enabled=false",
                "spring.flyway.enabled=false",
                "spring.jpa.igloo.data-upgrade.enabled=false",
                "spring.jpa.igloo.sql-exporter.enabled=true")
            .build()
            .run();
    context.getBean(SqlRunner.class).migrationScript(action, output);
  }
}
