package fr.openwide.core.jpa.migration.service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import org.hibernate.PropertyValueException;
import org.slf4j.Logger;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import fr.openwide.core.jpa.business.generic.model.GenericEntity;
import fr.openwide.core.jpa.business.generic.service.IGenericEntityService;
import fr.openwide.core.jpa.migration.monitor.ProcessorMonitorContext;
import fr.openwide.core.jpa.migration.rowmapper.AbstractResultRowMapper;
import fr.openwide.core.jpa.migration.util.IBatchAssociationMigrationInformation;

/**
 * An abstract base for migration services that import associations from an entity to other elements
 * (@OneToMany, @OneToOne, @ManyToOne, @ManyToMany, but also @ElementCollection)
 * 
 * @param T1 The type of the entity on the owning (non "mappedBy") side of the association
 * @param T2 The type of the owning entity association member (e.g. Collection&lt;AnotherType&gt;)
 */
public abstract class AbstractBatchAssociationMigrationService<T1 extends GenericEntity<Long, T1>, T2>
		extends AbstractMigrationService {
	
	private static final int DEFAULT_VALUES_PER_KEY = 3;

	public void importAllEntities() {
		Date startTime = new Date();
		
		Long rowCount = countRows(getMigrationInformation().getSqlCountRows());
		
		List<Long> entityIds = ImmutableList.copyOf(getJdbcTemplate().queryForList(getMigrationInformation().getSqlAllIds(), Long.class));
		List<List<Long>> entityIdsPartitions = Lists.partition(entityIds, getPartitionSize());
		List<Callable<Void>> callables = Lists.newArrayList();
		for (final List<Long> entityPartition : entityIdsPartitions) {
			Callable<Void> callable = new Callable<Void>() {
				@Override
				public Void call() throws Exception {
					importBatch(entityPartition);
					return null;
				}
			};
			callables.add(callable);
		}
		createThreadedProcessor(100).runWithTransaction(
				getMigrationInformation().getAssociationName(),
				callables, getWriteTransactionTemplate(), rowCount.intValue());
		
		logMigrationEnd(getMigrationInformation().getAssociationName(), startTime);
	}

	private void importBatch(List<Long> entityIds) throws PropertyValueException {
		preload(entityIds);
		
		try {
			MapSqlParameterSource entityIdsParameterSource = new MapSqlParameterSource();
			entityIdsParameterSource.addValue(getMigrationInformation().getParameterIds(), entityIds);
			AutowireCapableBeanFactory autowire = applicationContext.getAutowireCapableBeanFactory();
			
			AbstractResultRowMapper<? extends Map<T1, T2>> rowMapper = getMigrationInformation().newRowMapper(entityIds.size(), DEFAULT_VALUES_PER_KEY);
			
			autowire.autowireBean(rowMapper);
			autowire.initializeBean(rowMapper, rowMapper.getClass().getSimpleName());
			prepareRowMapper(rowMapper, entityIds);
			getNamedParameterJdbcTemplate().query(getMigrationInformation().getSqlRequest(), entityIdsParameterSource, rowMapper);
			
			for (Map.Entry<T1, T2> entry : rowMapper.getResults().entrySet()) {
				T1 entity = entry.getKey();
				getMigrationInformation().addToAssociation(entity, entry.getValue());
				if (getEntityService() != null) {
					getEntityService().update(entity);
				} else {
					entityManagerUtils.getEntityManager().persist(entity);
				}
			}
			
		} catch (Exception e) {
			getLogger().error("Erreur lors de la persistence d'un paquet de {}. {} créations annulées.",
					getMigrationInformation().getAssociationName(), entityIds.size(), e);
			ProcessorMonitorContext.get().getDoneItems().addAndGet(-1 * entityIds.size());
		}
	}

	protected void preload(List<Long> entityIds) {
		if (getMigrationInformation().getPreloadRequests() != null) {
			Map<Class<? extends GenericEntity<Long, ?>>, String> preloadRequests = getMigrationInformation().getPreloadRequests();
			for (Class<? extends GenericEntity<Long, ?>> preloadedClass : preloadRequests.keySet()) {
				String sqlPreloadRequest = preloadRequests.get(preloadedClass);
				if (sqlPreloadRequest == null) {
					listEntitiesByIds(preloadedClass, entityIds);
				} else {
					preloadLinkedEntities(preloadedClass,
							sqlPreloadRequest,
							getMigrationInformation().getParameterIds(),
							entityIds);
				}
			}
		}
	}

	protected void prepareRowMapper(RowMapper<?> rowMapper, List<Long> entityIds) {
	}

	protected abstract IBatchAssociationMigrationInformation<T1, T2> getMigrationInformation();

	protected abstract Logger getLogger();

	protected abstract IGenericEntityService<Long, T1> getEntityService();

	protected Integer getPartitionSize() {
		return 100;
	}
}
