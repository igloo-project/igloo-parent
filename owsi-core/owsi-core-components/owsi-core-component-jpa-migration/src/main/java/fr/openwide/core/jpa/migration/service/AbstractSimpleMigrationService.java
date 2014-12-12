package fr.openwide.core.jpa.migration.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import org.apache.commons.lang3.mutable.MutableInt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import fr.openwide.core.jpa.business.generic.model.GenericEntity;
import fr.openwide.core.jpa.business.generic.service.IGenericEntityService;
import fr.openwide.core.jpa.migration.util.IMigrationEntitySimpleInformation;
import fr.openwide.core.jpa.more.business.generic.model.GenericListItem;
import fr.openwide.core.jpa.more.business.generic.service.IGenericListItemService;

public abstract class AbstractSimpleMigrationService extends AbstractEntityMigrationService {

	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractSimpleMigrationService.class);

	@Autowired
	private IGenericListItemService genericListItemService;

	private List<Class<? extends GenericEntity<Long, ?>>> clazzList = new ArrayList<>();

	protected <T extends GenericEntity<Long, T>> Callable<Void> getEntityMigrationTask(MutableInt totalItems,
			final IMigrationEntitySimpleInformation<T> entityInformation) {
		final List<Long> entityIds = ImmutableList.copyOf(getJdbcTemplate().queryForList(entityInformation.getSqlAllIds(), Long.class));
		return getEntityMigrationTask(totalItems, entityIds, entityInformation, null);
	}

	protected <T extends GenericEntity<Long, T>> Callable<Void> getEntityMigrationTask(MutableInt totalItems,
			final IMigrationEntitySimpleInformation<T> entityInformation,
			final IGenericEntityService<Long, T> genericEntityService) {
		final List<Long> entityIds = ImmutableList.copyOf(getJdbcTemplate().queryForList(entityInformation.getSqlAllIds(), Long.class));
		return getEntityMigrationTask(totalItems, entityIds, entityInformation, genericEntityService);
	}

	protected <T extends GenericEntity<Long, T>> Callable<Void> getEntityMigrationTask(MutableInt totalItems,
			final List<Long> entityIds, final IMigrationEntitySimpleInformation<T> entityInformation,
			final IGenericEntityService<Long, T> genericEntityService) {
		
		totalItems.add(entityIds.size());
		
		return new Callable<Void>() {
			@Override
			public Void call() throws Exception {
				Map<Long, T> entities = Maps.newHashMapWithExpectedSize(entityIds.size());
				
				RowMapper<?> rowMapper = entityInformation.getRowMapperClass().getConstructor(Map.class).newInstance(entities);
				AutowireCapableBeanFactory autowire = applicationContext.getAutowireCapableBeanFactory();
				autowire.autowireBean(rowMapper);
				autowire.initializeBean(rowMapper, rowMapper.getClass().getSimpleName());
				
				if (entityInformation.getParameterIds() == null) {
					getJdbcTemplate().query(entityInformation.getSqlRequest(), rowMapper);
				} else {
					MapSqlParameterSource entityIdsParameterSource = new MapSqlParameterSource();
					entityIdsParameterSource.addValue(entityInformation.getParameterIds(), entityIds);
					getNamedParameterJdbcTemplate().query(entityInformation.getSqlRequest(), entityIdsParameterSource,
							rowMapper);
				}
				
				try {
					for (T entity : entities.values()) {
						if (GenericListItem.class.isAssignableFrom(entityInformation.getEntityClass())) {
							genericListItemService.create((GenericListItem<?>)entity);
						} else if (genericEntityService != null) {
							genericEntityService.create(entity);
						} else {
							entityManagerUtils.getEntityManager().persist(entity);
						}
						
					}
				} catch (Exception e) {
					LOGGER.error("Erreur lors de la persistence d'un(e) "
							+ entityInformation.getEntityClass().getSimpleName()
							+ ". {} créations annulées.", entities.size(), e);
				}
				clazzList.add(entityInformation.getEntityClass());
				
				return null;
			}
		};
	}

	protected <T extends GenericEntity<Long, T>> List<Callable<Void>> getEntityMigrationTasks(final MutableInt totalItems,
			final IMigrationEntitySimpleInformation<T> entityInformation) {
		return getEntityMigrationTasks(totalItems, entityInformation, null);
	}

	protected <T extends GenericEntity<Long, T>> List<Callable<Void>> getEntityMigrationTasks(final MutableInt totalItems,
			final IMigrationEntitySimpleInformation<T> entityInformation,
			final IGenericEntityService<Long, T> entityService) {
		
		if (entityInformation.getParameterIds() == null) {
			throw new IllegalStateException("ParameterIds null. Pour importer les entités par lot, il faut spécifier ParameterIds.");
		}
		
		final List<Long> entityIds = ImmutableList.copyOf(getJdbcTemplate().queryForList(entityInformation.getSqlAllIds(), Long.class));
		List<List<Long>> entityIdsPartitions = Lists.partition(entityIds, 100);
		List<Callable<Void>> callables = Lists.newArrayList();
		
		for (final List<Long> entityIdsPartition : entityIdsPartitions) {
			callables.add(getEntityMigrationTask(totalItems, entityIdsPartition, entityInformation, entityService));
		}
		clazzList.add(entityInformation.getEntityClass());
		
		return callables;
	}

	protected void updateReferentielSequences() {
		for (Class<? extends GenericEntity<Long, ?>> clazz : clazzList) {
			updateSequence(clazz);
		}
	}

}
