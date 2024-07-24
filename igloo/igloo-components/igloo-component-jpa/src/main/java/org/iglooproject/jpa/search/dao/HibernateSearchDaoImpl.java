package org.iglooproject.jpa.search.dao;

import static org.iglooproject.jpa.property.JpaSearchPropertyIds.HIBERNATE_SEARCH_REINDEX_BATCH_SIZE;
import static org.iglooproject.jpa.property.JpaSearchPropertyIds.HIBERNATE_SEARCH_REINDEX_LOAD_THREADS;

import com.google.common.collect.Sets;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.io.Serializable;
import java.util.Set;
import java.util.stream.Collectors;
import org.hibernate.CacheMode;
import org.hibernate.Hibernate;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.entity.SearchIndexedEntity;
import org.hibernate.search.mapper.orm.massindexing.MassIndexer;
import org.hibernate.search.mapper.orm.session.SearchSession;
import org.hibernate.search.mapper.pojo.massindexing.MassIndexingMonitor;
// import org.hibernate.search.jpa.Search;
import org.iglooproject.jpa.business.generic.model.GenericEntity;
import org.iglooproject.jpa.exception.ServiceException;
import org.iglooproject.spring.property.service.IPropertyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class HibernateSearchDaoImpl implements IHibernateSearchDao {

  private static final Logger LOGGER = LoggerFactory.getLogger(HibernateSearchDaoImpl.class);

  @Autowired private IPropertyService propertyService;

  @PersistenceContext private EntityManager entityManager;

  @Override
  public void reindexAll() throws ServiceException {
    reindexClasses(Object.class);
  }

  @Override
  public void reindexClasses(Class<?>... classes) throws ServiceException {
    try {
      SearchSession searchSession = Search.session(entityManager);
      reindexClasses(
          searchSession,
          getIndexedRootEntities(classes.length > 0 ? classes : new Class<?>[] {Object.class}));
    } catch (RuntimeException | InterruptedException e) {
      if (e instanceof InterruptedException) {
        Thread.currentThread().interrupt();
      }
      throw new ServiceException(e);
    }
  }

  protected void reindexClasses(SearchSession fullTextEntityManager, Set<Class<?>> entityClasses)
      throws InterruptedException {
    int batchSize = propertyService.get(HIBERNATE_SEARCH_REINDEX_BATCH_SIZE);
    int loadThreads = propertyService.get(HIBERNATE_SEARCH_REINDEX_LOAD_THREADS);

    if (LOGGER.isInfoEnabled()) {
      LOGGER.info("Targets for indexing job: {}", entityClasses);
    }

    for (Class<?> clazz : entityClasses) {
      ProgressMonitor progressMonitor = new ProgressMonitor();
      Thread t = new Thread(progressMonitor);
      LOGGER.info(String.format("Reindexing %1$s.", clazz));
      t.start();
      MassIndexer indexer = fullTextEntityManager.massIndexer(clazz);
      indexer
          .batchSizeToLoadObjects(batchSize)
          .threadsToLoadObjects(loadThreads)
          .cacheMode(CacheMode.NORMAL)
          .monitor(progressMonitor)
          .startAndWait();
      progressMonitor.stop();
      t.interrupt();
      LOGGER.info(String.format("Reindexing %1$s done.", clazz));
    }
  }

  @Override
  public Set<Class<?>> getIndexedRootEntities(Class<?>... selection) {
    return Search.mapping(entityManager.getEntityManagerFactory()).allIndexedEntities().stream()
        .map(SearchIndexedEntity::javaClass)
        .filter(
            indexedClass ->
                Set.of(selection).stream()
                    .anyMatch(selectionClass -> selectionClass.isAssignableFrom(indexedClass)))
        .collect(
            Collectors.toCollection(
                () ->
                    Sets.newTreeSet(
                        (o1, o2) ->
                            GenericEntity.STRING_COLLATOR_FRENCH.compare(
                                o1.getSimpleName(), o2.getSimpleName()))));
  }

  // TODO: igloo-boot : migration vers api HS 6 OK
  @Override
  public <K extends Serializable & Comparable<K>, E extends GenericEntity<K, ?>> void reindexEntity(
      E entity) {
    if (entity != null) {
      SearchSession session = Search.session(entityManager);
      session.indexingPlan().addOrUpdate(Hibernate.unproxy(entity));
    }
  }

  protected EntityManager getEntityManager() {
    return entityManager;
  }

  private static final class ProgressMonitor implements MassIndexingMonitor, Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProgressMonitor.class);

    private long documentsAdded;
    private long documentsBuilt;
    private long entitiesLoaded;
    private long totalCount;
    private boolean indexingCompleted;
    private boolean stopped;

    @Override
    public void documentsAdded(long increment) {
      this.documentsAdded += increment;
    }

    @Override
    public void documentsBuilt(long number) {
      this.documentsBuilt += number;
    }

    @Override
    public void entitiesLoaded(long size) {
      this.entitiesLoaded += size;
    }

    @Override
    public void addToTotalCount(long count) {
      this.totalCount += count;
    }

    @Override
    public void indexingCompleted() {
      this.indexingCompleted = true;
    }

    public void stop() {
      this.stopped = true;
      log();
    }

    @Override
    public void run() {
      if (LOGGER.isDebugEnabled()) {
        try {
          while (true) {
            log();
            Thread.sleep(15000);
            if (indexingCompleted) {
              LOGGER.debug("Indexing done");
              break;
            }
          }
        } catch (RuntimeException | InterruptedException e) {
          if (e instanceof InterruptedException) {
            Thread.currentThread().interrupt();
          }
          if (!stopped) {
            LOGGER.error("Error ; massindexer monitor stopped", e);
          }
          LOGGER.debug("Massindexer monitor thread interrupted");
        }
      }
    }

    private void log() {
      LOGGER.debug(
          String.format(
              "Indexing %1$d / %2$d (entities loaded: %3$d, documents built: %4$d)",
              documentsAdded, totalCount, entitiesLoaded, documentsBuilt));
    }
  }

  @Override
  public void flushToIndexes() {
    Search.session(entityManager).indexingPlan().execute();
  }
}
