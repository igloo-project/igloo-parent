package fr.openwide.core.jpa.migration.service;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
import com.google.common.collect.Sets;

import fr.openwide.core.jpa.business.generic.model.GenericEntity;
import fr.openwide.core.jpa.business.generic.service.IGenericEntityService;
import fr.openwide.core.jpa.exception.SecurityServiceException;
import fr.openwide.core.jpa.exception.ServiceException;
import fr.openwide.core.jpa.migration.rowmapper.AbstractListResultRowMapper;
import fr.openwide.core.jpa.migration.rowmapper.AbstractMapResultRowMapper;
import fr.openwide.core.jpa.migration.rowmapper.AbstractResultRowMapper;
import fr.openwide.core.jpa.migration.util.ISimpleEntityMigrationInformation;
import fr.openwide.core.jpa.more.business.generic.model.GenericListItem;
import fr.openwide.core.jpa.more.business.generic.service.IGenericListItemService;

public abstract class AbstractSimpleEntityMigrationService extends AbstractMigrationService {

	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractSimpleEntityMigrationService.class);

	@Autowired
	private IGenericListItemService genericListItemService;

	private Set<Class<? extends GenericEntity<Long, ?>>> clazzSet = Sets.newLinkedHashSet();

	protected <T extends GenericEntity<Long, ?>> Callable<Void> getEntityMigrationTask(MutableInt totalItems,
			final ISimpleEntityMigrationInformation<T> entityInformation) {
		final List<Long> entityIds = ImmutableList.copyOf(getJdbcTemplate().queryForList(entityInformation.getSqlAllIds(), Long.class));
		return getEntityMigrationTask(totalItems, entityIds, entityInformation, null);
	}

	protected <T extends GenericEntity<Long, ?>> Callable<Void> getEntityMigrationTask(MutableInt totalItems,
			final ISimpleEntityMigrationInformation<T> entityInformation,
			final IGenericEntityService<Long, T> genericEntityService) {
		final List<Long> entityIds = ImmutableList.copyOf(getJdbcTemplate().queryForList(entityInformation.getSqlAllIds(), Long.class));
		return getEntityMigrationTask(totalItems, entityIds, entityInformation, genericEntityService);
	}

	protected <T extends GenericEntity<Long, ?>> Callable<Void> getEntityMigrationTask(MutableInt totalItems,
			final List<Long> entityIds, final ISimpleEntityMigrationInformation<T> entityInformation,
			final IGenericEntityService<Long, T> genericEntityService) {
		
		totalItems.add(entityIds.size());
		
		return new Callable<Void>() {
			@Override
			public Void call() throws Exception {
				List<T> entitiesList = Lists.newArrayListWithExpectedSize(entityIds.size());
				Map<Long, T> entitiesMap = Maps.newHashMapWithExpectedSize(entityIds.size());

				RowMapper<?> rowMapper;
				Class<? extends AbstractResultRowMapper<?>> rowMapperClass = entityInformation.getRowMapperClass();
				
				if (AbstractMapResultRowMapper.class.isAssignableFrom(rowMapperClass)) {
					rowMapper = rowMapperClass.getConstructor(Map.class).newInstance(entitiesMap);
				} else if (AbstractListResultRowMapper.class.isAssignableFrom(rowMapperClass)) {
					rowMapper = rowMapperClass.getConstructor(List.class).newInstance(entitiesList);
				} else {
					throw new IllegalStateException(String.format("Type de rowmapper non reconnu %1$s", rowMapperClass.getSimpleName()));
				}
				AutowireCapableBeanFactory autowire = applicationContext.getAutowireCapableBeanFactory();
				autowire.autowireBean(rowMapper);
				autowire.initializeBean(rowMapper, rowMapper.getClass().getSimpleName());
				prepareRowMapper(rowMapper, entityIds);
				
				if (entityInformation.getParameterIds() == null) {
					getJdbcTemplate().query(entityInformation.getSqlRequest(), rowMapper);
				} else {
					MapSqlParameterSource entityIdsParameterSource = new MapSqlParameterSource();
					entityIdsParameterSource.addValue(entityInformation.getParameterIds(), entityIds);
					getNamedParameterJdbcTemplate().query(entityInformation.getSqlRequest(), entityIdsParameterSource,
							rowMapper);
				}
				
				Collection<T> entities;
				if (AbstractMapResultRowMapper.class.isAssignableFrom(rowMapperClass)) {
					entities = entitiesMap.values();
				} else if (AbstractListResultRowMapper.class.isAssignableFrom(rowMapperClass)) {
					entities = entitiesList;
				} else {
					throw new IllegalStateException(String.format("Type de rowmapper non reconnu %1$s", rowMapperClass.getSimpleName()));
				}
				
				try {
					for (T entity : entities) {
						if (GenericListItem.class.isAssignableFrom(entityInformation.getEntityClass())) {
							genericListItemService.create((GenericListItem<?>)entity);
						} else if (genericEntityService != null) {
							genericEntityService.create(entity);
						} else {
							entityManagerUtils.getEntityManager().persist(entity);
						}
						
					}
				} catch (RuntimeException | ServiceException | SecurityServiceException e) {
					LOGGER.error("Erreur lors de la persistence d'un(e) "
							+ entityInformation.getEntityClass().getSimpleName()
							+ ". {} créations annulées.", entities.size(), e);
				}
				clazzSet.add(entityInformation.getEntityClass());
				
				return null;
			}
		};
	}

	protected void prepareRowMapper(RowMapper<?> rowMapper, List<Long> entityIds) {
	}

	protected <T extends GenericEntity<Long, ?>> List<Callable<Void>> getEntityMigrationTasks(final MutableInt totalItems,
			final ISimpleEntityMigrationInformation<T> entityInformation) {
		return getEntityMigrationTasks(totalItems, entityInformation, null);
	}

	protected <T extends GenericEntity<Long, ?>> List<Callable<Void>> getEntityMigrationTasks(final MutableInt totalItems,
			final ISimpleEntityMigrationInformation<T> entityInformation,
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
		clazzSet.add(entityInformation.getEntityClass());
		
		return callables;
	}

	protected void updateReferentielSequences() {
		for (Class<? extends GenericEntity<Long, ?>> clazz : clazzSet) {
			updateSequence(clazz);
		}
	}

}
