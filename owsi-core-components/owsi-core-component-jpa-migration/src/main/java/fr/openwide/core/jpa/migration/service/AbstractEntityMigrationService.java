package fr.openwide.core.jpa.migration.service;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.interceptor.DefaultTransactionAttribute;
import org.springframework.transaction.interceptor.TransactionAttribute;
import org.springframework.transaction.support.TransactionTemplate;

import com.google.common.collect.ImmutableList;
import com.mysema.query.jpa.impl.JPAQuery;
import com.mysema.query.types.path.PathBuilder;

import fr.openwide.core.jpa.business.generic.model.GenericEntity;
import fr.openwide.core.jpa.business.generic.model.QGenericEntity;
import fr.openwide.core.jpa.migration.processor.ThreadedProcessor;
import fr.openwide.core.jpa.migration.util.ProcessorProgressLogger;
import fr.openwide.core.jpa.util.EntityManagerUtils;
import fr.openwide.core.spring.config.CoreConfigurer;

public abstract class AbstractEntityMigrationService {

	public static final int DEFAULT_TIMEOUT = 15;

	public static final TimeUnit DEFAULT_TIMEOUT_UNIT = TimeUnit.MINUTES;

	public static final Logger PROGRESS_LOGGER = LoggerFactory.getLogger(ProcessorProgressLogger.class);

	private static final String SQL_COUNT_ROWS = "SELECT count(*) FROM %1$s";

	public static final String SQL_UPDATE_SEQUENCE = "SELECT setval('%1$s_id_seq', (SELECT max(id) FROM %1$s))";

	@Autowired
	protected CoreConfigurer configurer;

	@Autowired
	protected ConfigurableApplicationContext applicationContext;

	@Autowired
	protected JdbcTemplate newDatabaseJdbcTemplate;

	@Autowired
	protected EntityManagerUtils entityManagerUtils;

	private TransactionTemplate readOnlyTransactionTemplate;

	private TransactionTemplate writeTransactionTemplate;

	public ThreadedProcessor createThreadedProcessor(int maxLoggingIncrement) {
		return createThreadedProcessor(maxLoggingIncrement, DEFAULT_TIMEOUT);
	}

	public ThreadedProcessor createThreadedProcessor(int maxLoggingIncrement, int timeoutInMinutes) {
		return new ThreadedProcessor(4, timeoutInMinutes, DEFAULT_TIMEOUT_UNIT, 1, TimeUnit.MINUTES, configurer,
				2, TimeUnit.SECONDS,  // intervalle minimum pour le logging
				30, TimeUnit.SECONDS, // intervalle de temps maxi entre deux logs
				maxLoggingIncrement,  // intervalle de nombre d'éléments traités maxi entre deux logs
				PROGRESS_LOGGER
		);
	}

	protected abstract JdbcTemplate getJdbcTemplate();

	protected abstract NamedParameterJdbcTemplate getNamedParameterJdbcTemplate();

	protected TransactionTemplate getReadOnlyTransactionTemplate() {
		return readOnlyTransactionTemplate;
	}

	protected TransactionTemplate getWriteTransactionTemplate() {
		return writeTransactionTemplate;
	}

	protected void updateSequence(Class<? extends GenericEntity<Long, ?>> genericEntityClass) {
		updateSequence(genericEntityClass.getSimpleName().toLowerCase());
	} 
	
	protected void updateSequence(String tableName) {
		if (StringUtils.contains(tableName, "user")) {
			// Cas particulier de la table User
			newDatabaseJdbcTemplate.execute(String.format(SQL_UPDATE_SEQUENCE, "user_"));
		} else {
			newDatabaseJdbcTemplate.execute(String.format(SQL_UPDATE_SEQUENCE, tableName));
		}
	}

	protected Long countRows(String tableName) {
		Long rowCount = getJdbcTemplate().queryForObject(String.format(SQL_COUNT_ROWS, tableName), Long.class);
		if (rowCount != null) {
			return rowCount;
		} else {
			return new Long(0);
		}
	}

	public final <E extends GenericEntity<Long, ?>> void preloadLinkedEntities(Class<E> clazz,
			String queryForLinkedEntityIds, String partitionIdsParamName, Collection<Long> partitionIds) {
		MapSqlParameterSource linkedEntityIdsParameterSource = new MapSqlParameterSource();
		linkedEntityIdsParameterSource.addValue(partitionIdsParamName, partitionIds);
		
		List<Long> linkedEntitiesIds = ImmutableList.copyOf(getNamedParameterJdbcTemplate().queryForList(queryForLinkedEntityIds,
				linkedEntityIdsParameterSource, Long.class));
		
		if (linkedEntitiesIds.isEmpty()) {
			return;
		}
		
		listEntitiesByIds(clazz, linkedEntitiesIds);
	}

	public final <E extends GenericEntity<Long, ?>> List<E> listEntitiesByIds(Class<E> clazz, Collection<Long> entityIds) {
		PathBuilder<E> path = new PathBuilder<E>(clazz, clazz.getSimpleName());
		QGenericEntity qGenericEntity = new QGenericEntity(path);
		
		return new JPAQuery(entityManagerUtils.getEntityManager()).from(path)
				.where(qGenericEntity.id.in(entityIds))
				.list(path);
	}

	protected void logMigrationEnd(String context, Date startTime) {
		long duration = new Date().getTime() - startTime.getTime();
		
		StringBuilder sb = new StringBuilder(String.format("%1$s - Eléments migrés ", context));
		if (duration < 1000) {
			sb.append(String.format("en %1$s ms", duration));
		} else if (duration < 60000){
			sb.append(String.format("en %1$s s", new BigDecimal(duration / 1000f).setScale(3, BigDecimal.ROUND_HALF_UP).toString()));
		} else {
			sb.append(String.format("en %1$s min", new BigDecimal(duration / 60000f).setScale(2, BigDecimal.ROUND_HALF_UP).toString()));
		}
		PROGRESS_LOGGER.info(sb.toString());
	}

	@Autowired
	public void setPlatformTransactionManager(PlatformTransactionManager transactionManager) {
		DefaultTransactionAttribute readOnlyTransactionAttribute = new DefaultTransactionAttribute(TransactionAttribute.PROPAGATION_REQUIRED);
		readOnlyTransactionAttribute.setReadOnly(true);
		readOnlyTransactionTemplate = new TransactionTemplate(transactionManager, readOnlyTransactionAttribute);
		
		DefaultTransactionAttribute writeTransactionAttribute = new DefaultTransactionAttribute(TransactionAttribute.PROPAGATION_REQUIRED);
		writeTransactionAttribute.setReadOnly(false);
		writeTransactionTemplate = new TransactionTemplate(transactionManager, writeTransactionAttribute);
	}

	@Autowired
	public final void setNewDatabaseJdbcTemplate(@Value("#{dataSource}") DataSource dataSource) {
		newDatabaseJdbcTemplate = new JdbcTemplate(dataSource);
	}
}
