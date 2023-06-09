package org.iglooproject.jpa.sql;

import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.Callable;

import org.iglooproject.jpa.autoconfigure.SqlExporter;
import org.iglooproject.jpa.sql.SqlRunner.Action;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;

import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

@EnableAutoConfiguration(
		excludeName = {
				"org.iglooproject.jpa.more.autoconfigure.JpaMoreAutoConfiguration",
				"org.iglooproject.jpa.security.autoconfigure.SecurityAutoConfiguration"
		}
)
@Command(name = "sql", mixinStandardHelpOptions = true)
public class BaseSqlExporterCommand implements Callable<Integer> {

	@Parameters(index = "0", description = "Action", defaultValue = "create")
	private Action action;
	@Parameters(index = "1", description = "Output file. Use stdout or - to output to stdout", defaultValue = "stdout")
	private String target;

	public BaseSqlExporterCommand() {
		super();
	}

	@Override
	public Integer call() throws Exception {
		Path output = List.of("-", "stdout").contains(target) ? null : Path.of(target);
		SqlExporter.export(new SpringApplicationBuilder(this.getClass()), action, output);
		return 0;
	
	}

}