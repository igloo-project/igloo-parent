package org.igloo.hibernate.hbm;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.EnumSet;
import java.util.Map;

import javax.persistence.EntityManagerFactory;

import org.hibernate.boot.Metadata;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.engine.spi.SessionFactoryImplementor;
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
import org.springframework.context.ConfigurableApplicationContext;

import com.google.common.collect.Maps;

public final class SqlUpdateScript {

	private SqlUpdateScript() {}

	public static void writeSqlDiffScript(ConfigurableApplicationContext context, String fileName, String action) {
		EntityManagerFactory entityManagerFactory = context.getBean(EntityManagerFactory.class);
		Metadata metadata = context.getBean(MetadataRegistryIntegrator.class).getMetadata();
		writeSqlDiffScript(entityManagerFactory, metadata, fileName, action);
	}

	public static void writeSqlDiffScript(EntityManagerFactory entityManagerFactory, Metadata metadata,
			String fileName, String action) {
		ExecutionOptions executionOptions = new ExecutionOptions() {
			
			@Override
			public boolean shouldManageNamespaces() {
				return false;
			}
			
			@Override
			public Map<?, ?> getConfigurationValues() {
				Map<String, String> config = Maps.newHashMap();
				config.put(AvailableSettings.HBM2DDL_DELIMITER, ";");
				return config;
			}
			
			@Override
			public ExceptionHandler getExceptionHandler() {
				// TODO récupérer et vérifier aucune exception pendant la mise à jour
				return new ExceptionHandlerCollectingImpl();
			}
		};
		
		ServiceRegistry serviceRegistry = entityManagerFactory.unwrap(SessionFactoryImplementor.class).getServiceRegistry();
		
		// hibernate append script to existing file; we want file to be reset.
		// we create the file if needed and output null content to it.
		try {
			Files.write(Paths.get(fileName), new byte[0], StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
		} catch (IOException e) {
			throw new RuntimeException(String.format("File %s cannot be created/emptied.", fileName), e);
		}
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
	}
}
