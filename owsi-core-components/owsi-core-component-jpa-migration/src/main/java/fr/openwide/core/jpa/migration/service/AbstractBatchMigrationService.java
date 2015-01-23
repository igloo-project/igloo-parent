package fr.openwide.core.jpa.migration.service;

import java.util.Collection;
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
import com.google.common.collect.Maps;

import fr.openwide.core.jpa.business.generic.model.GenericEntity;
import fr.openwide.core.jpa.business.generic.service.IGenericEntityService;
import fr.openwide.core.jpa.migration.monitor.ProcessorMonitorContext;
import fr.openwide.core.jpa.migration.rowmapper.AbstractEntityListRowMapper;
import fr.openwide.core.jpa.migration.rowmapper.AbstractEntityMapRowMapper;
import fr.openwide.core.jpa.migration.rowmapper.AbstractEntityRowMapper;
import fr.openwide.core.jpa.migration.util.IMigrationEntityBatchInformation;

public abstract class AbstractBatchMigrationService<T extends GenericEntity<Long, T>> extends AbstractEntityMigrationService {

	public void importAllEntities() {
		Date startTime = new Date();
		
		Long rowCount = countRows(getEntityInformation().getSqlCountRows());
		
		List<Long> entityIds = ImmutableList.copyOf(getJdbcTemplate().queryForList(getEntityInformation().getSqlAllIds(), Long.class));
		List<List<Long>> entityIdsPartitions = Lists.partition(entityIds, getPartitionSize());
		List<Callable<Void>> callables = Lists.newArrayList();
		for (final List<Long> entityPartition : entityIdsPartitions) {
			Callable<Void> callable = new Callable<Void>() {
				@Override
				public Void call() throws Exception {
					importLotEntity(entityPartition);
					return null;
				}
			};
			callables.add(callable);
		}
		createThreadedProcessor(100).runWithTransaction(
				getEntityInformation().getEntityClass().getSimpleName(),
				callables, getWriteTransactionTemplate(), rowCount.intValue());
		
		updateSequence(getEntityInformation().getEntityClass());
		
		logMigrationEnd(getEntityInformation().getEntityClass().getSimpleName(), startTime);
	};

	private void importLotEntity(List<Long> entityIds) throws PropertyValueException {
		List<T> entitiesList = Lists.newArrayList();
		Map<Long, T> entitiesMap = Maps.newHashMapWithExpectedSize(entityIds.size());
		
		preload(entityIds);
		
		try {
			MapSqlParameterSource entityIdsParameterSource = new MapSqlParameterSource();
			entityIdsParameterSource.addValue(getEntityInformation().getParameterIds(), entityIds);
			AutowireCapableBeanFactory autowire = applicationContext.getAutowireCapableBeanFactory();
			
			RowMapper<?> rowMapper;
			Class<? extends AbstractEntityRowMapper<?>> rowMapperClass = getEntityInformation().getRowMapperClass();
			
			if (AbstractEntityMapRowMapper.class.isAssignableFrom(rowMapperClass)) {
				rowMapper = rowMapperClass.getConstructor(Map.class).newInstance(entitiesMap);
			} else if (AbstractEntityListRowMapper.class.isAssignableFrom(rowMapperClass)) {
				rowMapper = rowMapperClass.getConstructor(List.class).newInstance(entitiesList);
			} else {
				throw new IllegalStateException(String.format("Type de rowmapper non reconnu %1$s", rowMapperClass.getSimpleName()));
			}
			
			autowire.autowireBean(rowMapper);
			autowire.initializeBean(rowMapper, rowMapper.getClass().getSimpleName());
			prepareRowMapper(rowMapper, entityIds);
			getNamedParameterJdbcTemplate().query(getEntityInformation().getSqlRequest(), entityIdsParameterSource, rowMapper);
			
			Collection<T> entities;
			if (AbstractEntityMapRowMapper.class.isAssignableFrom(rowMapperClass)) {
				entities = entitiesMap.values();
			} else if (AbstractEntityListRowMapper.class.isAssignableFrom(rowMapperClass)) {
				entities = entitiesList;
			} else {
				throw new IllegalStateException(String.format("Type de rowmapper non reconnu %1$s", rowMapperClass.getSimpleName()));
			}
			
			for (T entity : entities) {
				if (getEntityService() != null) {
					getEntityService().create(entity);
				} else {
					entityManagerUtils.getEntityManager().persist(entity);
				}
			}
			
		} catch (Exception e) {
			getLogger().error("Erreur lors de la persistence d'un paquet de {}. {} créations annulées.",
					getEntityInformation().getEntityClass().getSimpleName(), entityIds.size(), e);
			ProcessorMonitorContext.get().getDoneItems().addAndGet(-1 * entityIds.size());
		}
	};

	protected void preload(List<Long> entityIds) {
		if (getEntityInformation().getPreloadRequests() != null) {
			Map<Class<? extends GenericEntity<Long, ?>>, String> preloadRequests = getEntityInformation().getPreloadRequests();
			for (Class<? extends GenericEntity<Long, ?>> preloadedClass : preloadRequests.keySet()) {
				String sqlPreloadRequest = preloadRequests.get(preloadedClass);
				if (sqlPreloadRequest == null) {
					listEntitiesByIds(preloadedClass, entityIds);
				} else {
					preloadLinkedEntities(preloadedClass,
							sqlPreloadRequest,
							getEntityInformation().getParameterIds(),
							entityIds);
				}
			}
		}
	}

	protected void prepareRowMapper(RowMapper<?> rowMapper, List<Long> entityIds) {
	}

	protected abstract IMigrationEntityBatchInformation<T> getEntityInformation();

	protected abstract Logger getLogger();

	protected abstract IGenericEntityService<Long, T> getEntityService();

	protected Integer getPartitionSize() {
		return 100;
	}
}
