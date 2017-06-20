package fr.openwide.core.basicapp.init;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import fr.openwide.core.basicapp.init.config.spring.BasicApplicationInitConfig;
import fr.openwide.core.jpa.exception.SecurityServiceException;
import fr.openwide.core.jpa.exception.ServiceException;
import fr.openwide.core.jpa.migration.SqlUpdateScript;

public final class BasicApplicationSqlUpdateScriptMain {

	private static final Logger LOGGER = LoggerFactory.getLogger(BasicApplicationSqlUpdateScriptMain.class);

	private BasicApplicationSqlUpdateScriptMain() {
	}
	/*
	 * This script uses Hibernate's hbm2ddl to write in an sql file either the script which will create the jpa model
	 * in the database or the differences between the jpa model and the existing database.
	 */
	public static void main(String[] args) throws ServiceException, SecurityServiceException, IOException {
		ConfigurableApplicationContext context = null;
		try {
			context = new AnnotationConfigApplicationContext(BasicApplicationInitConfig.class);
			String fileName;
			String action;
			
			if(args.length == 2) {
				fileName = args[1];
				action = args[0];
			} else {
				fileName = "/tmp/script.sql";
				action = "create";
			}
			
			SqlUpdateScript.writeSqlDiffScript(context, fileName, action);
			
			LOGGER.info("Initialization complete");
		} catch (Throwable e) { // NOSONAR We just want to log the Exception/Error, no error handling here.
			LOGGER.error("Error during initialization", e);
			throw new IllegalStateException(e);
		} finally {
			if (context != null) {
				context.close();
			}
		}
	}

}
