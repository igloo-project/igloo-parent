package org.iglooproject.jpa.migration.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import igloo.jpa.batch.executor.BatchExecutorCreator;
import igloo.jpa.batch.executor.MultithreadedBatchExecutor;
import igloo.jpa.batch.monitor.ProcessorMonitorContext;
import igloo.jpa.batch.runnable.ReadWriteBatchRunnable;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.hibernate.PropertyValueException;
import org.iglooproject.jpa.business.generic.model.GenericEntity;
import org.iglooproject.jpa.business.generic.service.IGenericEntityService;
import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.iglooproject.jpa.migration.rowmapper.AbstractListResultRowMapper;
import org.iglooproject.jpa.migration.rowmapper.AbstractMapResultRowMapper;
import org.iglooproject.jpa.migration.rowmapper.AbstractResultRowMapper;
import org.iglooproject.jpa.migration.util.IBatchEntityMigrationInformation;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

public abstract class AbstractBatchEntityMigrationService<T extends GenericEntity<Long, T>>
    extends AbstractMigrationService {

  @Autowired private BatchExecutorCreator batchCreator;

  public void importAllEntities() {
    List<Long> entityIds =
        List.copyOf(
            getJdbcTemplate().queryForList(getMigrationInformation().getSqlAllIds(), Long.class));

    MultithreadedBatchExecutor executor = batchCreator.newMultithreadedBatchExecutor();
    executor.threads(4).batchSize(100);
    executor.run(
        getMigrationInformation().getEntityClass().getSimpleName(),
        entityIds,
        new ReadWriteBatchRunnable<Long>() {
          @Override
          public void executePartition(List<Long> partition) {
            importBatch(partition);
          }

          @Override
          public void postExecute() {
            updateSequence(getMigrationInformation().getEntityClass());
          }
        });
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
      Class<? extends AbstractResultRowMapper<?>> rowMapperClass =
          getMigrationInformation().getRowMapperClass();

      if (AbstractMapResultRowMapper.class.isAssignableFrom(rowMapperClass)) {
        rowMapper = rowMapperClass.getConstructor(Map.class).newInstance(entitiesMap);
      } else if (AbstractListResultRowMapper.class.isAssignableFrom(rowMapperClass)) {
        rowMapper = rowMapperClass.getConstructor(List.class).newInstance(entitiesList);
      } else {
        throw new IllegalStateException(
            String.format("Type de rowmapper non reconnu %1$s", rowMapperClass.getSimpleName()));
      }

      autowire.autowireBean(rowMapper);
      autowire.initializeBean(rowMapper, rowMapper.getClass().getSimpleName());
      prepareRowMapper(rowMapper, entityIds);
      getNamedParameterJdbcTemplate()
          .query(getMigrationInformation().getSqlRequest(), entityIdsParameterSource, rowMapper);

      Collection<T> entities;
      if (AbstractMapResultRowMapper.class.isAssignableFrom(rowMapperClass)) {
        entities = entitiesMap.values();
      } else if (AbstractListResultRowMapper.class.isAssignableFrom(rowMapperClass)) {
        entities = entitiesList;
      } else {
        throw new IllegalStateException(
            String.format("Type de rowmapper non reconnu %1$s", rowMapperClass.getSimpleName()));
      }

      for (T entity : entities) {
        if (getEntityService() != null) {
          getEntityService().create(entity);
        } else {
          entityManagerUtils.getEntityManager().persist(entity);
        }
      }

    } catch (RuntimeException
        | InstantiationException
        | IllegalAccessException
        | InvocationTargetException
        | NoSuchMethodException
        | ServiceException
        | SecurityServiceException e) {
      getLogger()
          .error(
              "Erreur lors de la persistence d'un paquet de {}. {} créations annulées.",
              getMigrationInformation().getEntityClass().getSimpleName(),
              entityIds.size(),
              e);
      ProcessorMonitorContext.get().getDoneItems().addAndGet(-1 * entityIds.size());
    }
  }

  protected void prepareRowMapper(RowMapper<?> rowMapper, List<Long> entityIds) {}

  protected abstract IBatchEntityMigrationInformation<T> getMigrationInformation();

  protected abstract Logger getLogger();

  /**
   * Override this if you want to use a GenericEntityService when creating entities.
   *
   * <p><strong>Note:</strong>This is not recommended, since these services generally assume that
   * they are really creating a brand new row, not importing it. They may, for instance, set the
   * "creationdate" attribute of an entity to the current time, which is probably wrong when
   * migrating.
   */
  protected IGenericEntityService<Long, T> getEntityService() {
    return null;
  }
}
