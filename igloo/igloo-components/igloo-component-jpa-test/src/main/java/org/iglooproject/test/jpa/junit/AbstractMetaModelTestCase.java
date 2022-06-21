package org.iglooproject.test.jpa.junit;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;

import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.beanutils.ResultSetDynaClass;
import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.boot.model.relational.Namespace;
import org.hibernate.boot.model.relational.Namespace.Name;
import org.hibernate.boot.model.relational.SqlStringGenerationContext;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.internal.SessionImpl;
import org.hibernate.resource.transaction.spi.DdlTransactionIsolator;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.tool.schema.extract.spi.DatabaseInformation;
import org.hibernate.tool.schema.extract.spi.SequenceInformation;
import org.hibernate.tool.schema.extract.spi.TableInformation;
import org.hibernate.tool.schema.internal.Helper;
import org.hibernate.tool.schema.internal.HibernateSchemaManagementTool;
import org.hibernate.tool.schema.internal.exec.JdbcContext;
import org.hibernate.tool.schema.spi.SchemaManagementTool;
import org.igloo.hibernate.hbm.MetadataRegistryIntegrator;
import org.iglooproject.jpa.config.spring.provider.IJpaConfigurationProvider;
import org.iglooproject.jpa.util.EntityManagerUtils;
import org.iglooproject.test.jpa.util.bean.DynaBeanConverter;
import org.iglooproject.test.jpa.util.jdbc.model.JdbcRelation;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import com.google.common.collect.Streams;

public abstract class AbstractMetaModelTestCase extends AbstractTestCase {
	
	@Autowired
	protected EntityManagerUtils entityManagerUtils;

	@Autowired
	protected IJpaConfigurationProvider configurationProvider;

	@Autowired
	protected MetadataRegistryIntegrator metadataRegistryIntegrator;

	public AbstractMetaModelTestCase() {
		super();
	}

	protected JdbcRelation getRelation(Connection connection, String schemaPattern, String relationPattern, String... types) {
		return getRelations(connection, schemaPattern, "%", types).stream()
				.filter(i -> i.getTable_name() != null && i.getTable_name().equalsIgnoreCase(relationPattern))
				.findFirst()
				.orElseThrow(() -> new NoSuchElementException(String.format("Relation %s cannot be found.", relationPattern)));
	}

	protected Collection<JdbcRelation> getRelations(Connection connection, String schemaPattern, String relationPattern, String... types) {
		org.springframework.util.Assert.notNull(connection, "connection must be provided");
		try {
			Iterator<DynaBean> tablesResultSet =
					new ResultSetDynaClass(
						connection.getMetaData().getTables(null, schemaPattern, relationPattern, types),
						true,
						true
					).iterator();
			
			return Streams.stream(tablesResultSet)
							.map(new DynaBeanConverter<>(JdbcRelation.class))
							.collect(ImmutableList.toImmutableList());
		} catch (SQLException e) {
			throw new IllegalStateException(
					String.format("error retrieving relation list for <%s-%s-%s>",
							schemaPattern, relationPattern, Joiner.on(", ").join(types)),
					e
			);
		}
	}
	
	protected TableInformation getTableInformation(String schema, String relation) {
		Identifier catalog = metadataRegistryIntegrator.getMetadata().getDatabase().getDefaultNamespace().getName().getCatalog();
		Identifier schemaIdentifier = metadataRegistryIntegrator.getMetadata().getDatabase().toIdentifier(schema);
		Identifier relationIdentifier = metadataRegistryIntegrator.getMetadata().getDatabase().toIdentifier(relation);
		return getDatabaseInformation().getTableInformation(new Namespace.Name(catalog, schemaIdentifier), relationIdentifier);
	}
	
	protected SequenceInformation getSequenceInformation(String schema, String relation) {
		Identifier catalog = metadataRegistryIntegrator.getMetadata().getDatabase().getDefaultNamespace().getName().getCatalog();
		Identifier schemaIdentifier = metadataRegistryIntegrator.getMetadata().getDatabase().toIdentifier(schema);
		Identifier relationIdentifier = metadataRegistryIntegrator.getMetadata().getDatabase().toIdentifier(relation);
		return getDatabaseInformation().getSequenceInformation(new Namespace.Name(catalog, schemaIdentifier), relationIdentifier);
	}
	
	protected DatabaseInformation getDatabaseInformation() {
		SessionFactoryImplementor sessionFactory = ((SessionImpl) entityManagerUtils.getEntityManager().getDelegate()).getSessionFactory();
		SqlStringGenerationContext sqlStringGenerationContext = sessionFactory.getSqlStringGenerationContext();
		ServiceRegistry serviceRegistry = sessionFactory.getServiceRegistry();
		HibernateSchemaManagementTool schemaManagementTool = ((HibernateSchemaManagementTool) serviceRegistry.getService(SchemaManagementTool.class));
		@SuppressWarnings("rawtypes")
		JdbcContext jdbcContext = schemaManagementTool.resolveJdbcContext((Map) Maps.newHashMap());
		DdlTransactionIsolator transactionIsolator = schemaManagementTool.getDdlTransactionIsolator(jdbcContext);
		return Helper.buildDatabaseInformation(serviceRegistry, transactionIsolator, sqlStringGenerationContext, schemaManagementTool);
	}

}