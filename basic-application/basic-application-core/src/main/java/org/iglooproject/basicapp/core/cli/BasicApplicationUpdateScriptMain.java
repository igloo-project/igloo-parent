package org.iglooproject.basicapp.core.cli;

import java.util.concurrent.Callable;

import org.iglooproject.basicapp.core.config.spring.BasicApplicationJpaConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import picocli.CommandLine;

@Configuration
@Import(BasicApplicationJpaConfiguration.class) // entity/model scanning
public class BasicApplicationUpdateScriptMain extends SqlExporterCommand implements Callable<Integer> {

	public static void main(String[] args) {
		CommandLine cl = new CommandLine(new BasicApplicationUpdateScriptMain());
		System.exit(cl.execute(args));
	}
}
