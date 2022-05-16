package org.igloo.hibernate.hbm;

import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.persistence.EntityManagerFactory;

import org.hibernate.boot.Metadata;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.tool.schema.SourceType;
import org.hibernate.tool.schema.TargetType;
import org.hibernate.tool.schema.internal.ExceptionHandlerCollectingImpl;
import org.hibernate.tool.schema.internal.exec.ScriptTargetOutputToWriter;
import org.hibernate.tool.schema.spi.ExceptionHandler;
import org.hibernate.tool.schema.spi.ExecutionOptions;
import org.hibernate.tool.schema.spi.SchemaManagementTool;
import org.hibernate.tool.schema.spi.ScriptSourceInput;
import org.hibernate.tool.schema.spi.ScriptTargetOutput;
import org.hibernate.tool.schema.spi.SourceDescriptor;
import org.hibernate.tool.schema.spi.TargetDescriptor;
import org.springframework.context.ConfigurableApplicationContext;

public final class SqlUpdateScript {

	private SqlUpdateScript() {}

	public static void writeSqlDiffScript(ConfigurableApplicationContext context, String fileName, String action) {
		EntityManagerFactory entityManagerFactory = context.getBean(EntityManagerFactory.class);
		Metadata metadata = context.getBean(MetadataRegistryIntegrator.class).getMetadata();
		writeSqlDiffScript(entityManagerFactory, metadata, SqlOutput.file(fileName), action);
	}

	public static void writeSqlDiffScript(ConfigurableApplicationContext context, SqlOutput output, String action) {
		EntityManagerFactory entityManagerFactory = context.getBean(EntityManagerFactory.class);
		Metadata metadata = context.getBean(MetadataRegistryIntegrator.class).getMetadata();
		writeSqlDiffScript(entityManagerFactory, metadata, output, action);
	}

	public static void writeSqlDiffScript(EntityManagerFactory entityManagerFactory, Metadata metadata,
			SqlOutput output, String action) {
		ExceptionHandlerCollectingImpl exceptionHandler = new ExceptionHandlerCollectingImpl();
		ExecutionOptions executionOptions = new ExecutionOptions() {
			
			@Override
			public boolean shouldManageNamespaces() {
				return false;
			}
			
			@Override
			public Map<?, ?> getConfigurationValues() {
				Map<String, String> config = new HashMap<>();
				config.put(AvailableSettings.HBM2DDL_DELIMITER, ";");
				return config;
			}
			
			@Override
			public ExceptionHandler getExceptionHandler() {
				return exceptionHandler;
			}
		};
		if (!exceptionHandler.getExceptions().isEmpty()) {
			IllegalStateException e = new IllegalStateException("One or multiple exception during execution");
			exceptionHandler.getExceptions().stream().forEach(e::addSuppressed);
			throw e;
		}
		
		ServiceRegistry serviceRegistry = entityManagerFactory.unwrap(SessionFactoryImplementor.class).getServiceRegistry();
		
		StringWriter writer = new StringWriter();
		TargetDescriptor targetDescriptor = new TargetDescriptorImpl(writer);
		
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
			serviceRegistry.getService(SchemaManagementTool.class).getSchemaCreator(new HashMap<>())
					.doCreation(metadata, executionOptions, sourceDescriptor, targetDescriptor);
		} else if (action.equals("update")) {
			serviceRegistry.getService(SchemaManagementTool.class).getSchemaMigrator(new HashMap<>())
					.doMigration(metadata, executionOptions, targetDescriptor);
		}
		
		String content = writer.getBuffer().toString();
		String schema = Optional.ofNullable(entityManagerFactory.getProperties().getOrDefault(AvailableSettings.DEFAULT_SCHEMA, null))
				.filter(String.class::isInstance).map(String.class::cast).orElse(null);
		if (schema != null) {
			content = content.replace(String.format("%s.", schema), "${schema}.");
		}
		content = content.replace("text.", "${text}.");
		
		if (output.isFile()) {
			try (Writer outputWriter = new FileWriter(output.getTarget(), false)) {
				outputWriter.write(content);
			} catch (IOException e) {
				throw new RuntimeException(String.format("File %s cannot be created/emptied.", output.getTarget()), e);
			}
		} else {
			System.out.println(content);
		}
	}

	private static class TargetDescriptorImpl implements TargetDescriptor {

		private final ScriptTargetOutputToWriter targetOutput;
		private final EnumSet<TargetType> types = EnumSet.of(TargetType.SCRIPT);
		
		public TargetDescriptorImpl(Writer writer) {
			targetOutput = new ScriptTargetOutputToWriter(writer);
		}

		@Override
		public EnumSet<TargetType> getTargetTypes() {
			return types;
		}

		@Override
		public ScriptTargetOutput getScriptTargetOutput() {
			return targetOutput;
		}
		
	}
}
