package fr.openwide.core.jpa.migration.service;

import java.lang.reflect.InvocationTargetException;
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
import fr.openwide.core.jpa.exception.SecurityServiceException;
import fr.openwide.core.jpa.exception.ServiceException;
import fr.openwide.core.jpa.migration.monitor.ProcessorMonitorContext;
import fr.openwide.core.jpa.migration.rowmapper.AbstractListResultRowMapper;
import fr.openwide.core.jpa.migration.rowmapper.AbstractMapResultRowMapper;
import fr.openwide.core.jpa.migration.rowmapper.AbstractResultRowMapper;
import fr.openwide.core.jpa.migration.util.IBatchEntityMigrationInformation;

public abstract class AbstractBatchEntityMigrationService<T extends GenericEntity<Long, T>> extends AbstractMigrationService {

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
				getMigrationInformation().getEntityClass().getSimpleName(),
				callables, getWriteTransactionTemplate(), rowCount.intValue());
		
		updateSequence(getMigrationInformation().getEntityClass());
		
		logMigrationEnd(getMigrationInformation().getEntityClass().getSimpleName(), startTime);
	}

	private void importBatch(List<Long> entityIds) throws PropertyValueException {
		List<T> entitiesList = Lists.newArrayList();
		Map<Long, T> entitiesMap = Maps.newHashMapWithExpectedSize(entityIds.size());
		
		preload(entityIds, getMigrationInformation());
		
		try {
			MapSqlParameterSource entityIdsParameterSource = new MapSqlParameterSource();
			entityIdsParameterSource.addValue(getMigrationInformation().getParameterIds(), entityIds);
			AutowireCapableBeanFactory autowire = applicationContext.getAutowireCapableBeanFactory();
			
			RowMapper<?> rowMapper;
			Class<? extends AbstractResultRowMapper<?>> rowMapperClass = getMigrationInformation().getRowMapperClass();
			
			if (AbstractMapResultRowMapper.class.isAssignableFrom(rowMapperClass)) {
				rowMapper = rowMapperClass.getConstructor(Map.class).newInstance(entitiesMap);
			} else if (AbstractListResultRowMapper.class.isAssignableFrom(rowMapperClass)) {
				rowMapper = rowMapperClass.getConstructor(List.class).newInstance(entitiesList);
			} else {
				throw new IllegalStateException(String.format("Type de rowmapper non reconnu %1$s", rowMapperClass.getSimpleName()));
			}
			
			autowire.autowireBean(rowMapper);
			autowire.initializeBean(rowMapper, rowMapper.getClass().getSimpleName());
			prepareRowMapper(rowMapper, entityIds);
			getNamedParameterJdbcTemplate().query(getMigrationInformation().getSqlRequest(), entityIdsParameterSource, rowMapper);
			
			Collection<T> entities;
			if (AbstractMapResultRowMapper.class.isAssignableFrom(rowMapperClass)) {
				entities = entitiesMap.values();
			} else if (AbstractListResultRowMapper.class.isAssignableFrom(rowMapperClass)) {
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
			
		} catch (RuntimeException | InstantiationException | IllegalAccessException
				| InvocationTargetException | NoSuchMethodException
				| ServiceException | SecurityServiceException e) {
			getLogger().error("Erreur lors de la persistence d'un paquet de {}. {} créations annulées.",
					getMigrationInformation().getEntityClass().getSimpleName(), entityIds.size(), e);
			ProcessorMonitorContext.get().getDoneItems().addAndGet(-1 * entityIds.size());
		}
	}

	protected void prepareRowMapper(RowMapper<?> rowMapper, List<Long> entityIds) {
	}

	protected abstract IBatchEntityMigrationInformation<T> getMigrationInformation();

	protected abstract Logger getLogger();

	/**
	 * Override this if you want to use a GenericEntityService when creating entities.
	 * <p><strong>Note:</strong>This is not recommended, since these services generally assume that they are really
	 * creating a brand new row, not importing it. They may, for instance, set the "creationdate" attribute of an
	 * entity to the current time, which is probably wrong when migrating.
	 */
	protected IGenericEntityService<Long, T> getEntityService() {
		return null;
	}

	protected Integer getPartitionSize() {
		return 100;
	}
}
