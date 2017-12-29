package org.iglooproject.jpa.migration;

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
import org.springframework.context.ConfigurableApplicationContext;

import com.google.common.collect.Maps;

import org.iglooproject.jpa.hibernate.integrator.spi.MetadataRegistryIntegrator;

public final class SqlUpdateScript {

	public static void writeSqlDiffScript(ConfigurableApplicationContext context, String fileName, String action) {
		EntityManagerFactory entityManagerFactory = context.getBean(EntityManagerFactory.class);
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		
		ExecutionOptions executionOptions = new ExecutionOptions() {
			
			@Override
			public boolean shouldManageNamespaces() {
				return false;
			}
			
			@Override
			public Map<?, ?> getConfigurationValues() {
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
	}
}
