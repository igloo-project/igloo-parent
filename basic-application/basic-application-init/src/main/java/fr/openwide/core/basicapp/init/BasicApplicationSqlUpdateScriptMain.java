package fr.openwide.core.basicapp.init;

import java.io.IOException;
import java.util.EnumSet;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.hibernate.boot.Metadata;
import org.hibernate.internal.SessionImpl;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.hibernate.tool.schema.SourceType;
import org.hibernate.tool.schema.TargetType;
import org.hibernate.tool.schema.internal.ExceptionHandlerCollectingImpl;
import org.hibernate.tool.schema.spi.ExceptionHandler;
import org.hibernate.tool.schema.spi.ExecutionOptions;
import org.hibernate.tool.schema.spi.SchemaManagementTool;
import org.hibernate.tool.schema.spi.ScriptSourceInput;
import org.hibernate.tool.schema.spi.SourceDescriptor;
import org.hibernate.tool.schema.spi.TargetDescriptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.google.common.collect.Maps;

import fr.openwide.core.basicapp.init.config.spring.BasicApplicationInitConfig;
import fr.openwide.core.basicapp.init.integrator.spi.MetadataRegistryIntegrator;
import fr.openwide.core.jpa.exception.SecurityServiceException;
import fr.openwide.core.jpa.exception.ServiceException;

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
				
			EntityManagerFactory entityManagerFactory = context.getBean(EntityManagerFactory.class);
			EntityManager entityManager = entityManagerFactory.createEntityManager();
			
			ExecutionOptions executionOptions = new ExecutionOptions() {
				
				@Override
				public boolean shouldManageNamespaces() {
					return false;
				}
				
				@Override
				public Map getConfigurationValues() {
					return Maps.newHashMap();
				}
				
				@Override
				public ExceptionHandler getExceptionHandler() {
					// TODO récupérer et vérifier aucune exception pendant la mise à jour
					return new ExceptionHandlerCollectingImpl();
				}
			};
			
			ServiceRegistry serviceRegistry = ((SessionImpl) entityManager.getDelegate()).getSessionFactory().getServiceRegistry();
			Metadata metadata = MetadataRegistryIntegrator.METADATA;
			
			EnumSet<TargetType> targetTypes = EnumSet.of(TargetType.SCRIPT);
			TargetDescriptor targetDescriptor = SchemaExport.buildTargetDescriptor(targetTypes, fileName, serviceRegistry);
			
			if (action.equals("create")) {
				final SourceDescriptor sourceDescriptor = new SourceDescriptor() {
					@Override
					public SourceType getSourceType() {
						return SourceType.METADATA;
					}
					
					@Override
					public ScriptSourceInput getScriptSourceInput() {
						return null;
					}
				};
				serviceRegistry.getService(SchemaManagementTool.class).getSchemaCreator(Maps.newHashMap())
						.doCreation(metadata, executionOptions, sourceDescriptor, targetDescriptor);
			} else if (action.equals("update")) {
				serviceRegistry.getService(SchemaManagementTool.class).getSchemaMigrator(Maps.newHashMap())
						.doMigration(metadata, executionOptions, targetDescriptor);
			}
			
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
