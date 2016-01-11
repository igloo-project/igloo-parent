package fr.openwide.core.jpa.batch.executor;

import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;

import com.google.common.collect.Lists;

import fr.openwide.core.commons.util.functional.Joiners;
import fr.openwide.core.jpa.business.generic.model.GenericEntity;
import fr.openwide.core.jpa.business.generic.model.GenericEntityCollectionReference;
import fr.openwide.core.jpa.exception.ServiceException;
import fr.openwide.core.jpa.query.IQuery;
import fr.openwide.core.jpa.query.Queries;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class SimpleHibernateBatchExecutor extends AbstractBatchExecutor<SimpleHibernateBatchExecutor> {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(SimpleHibernateBatchExecutor.class);

	private boolean flushToIndexes;
	
	private List<Class<?>> classesToReindex = Lists.newArrayListWithCapacity(0);
	
	public SimpleHibernateBatchExecutor flushToIndexes(boolean flushToIndexes) {
		this.flushToIndexes = flushToIndexes;
		return this;
	}
	
	public SimpleHibernateBatchExecutor reindexClasses(Class<?> clazz, Class<?>... classes) {
		classesToReindex = Lists.asList(clazz, classes);
		return this;
	}

	/**
	 * Runs a batch execution against a given list of entities (given by their IDs).
	 */
	public <E extends GenericEntity<Long, ?>> void run(final Class<E> clazz, final List<Long> entityIds,
			final IBatchRunnable<E> batchRunnable) {
		GenericEntityCollectionReference<Long, E> reference =
				new GenericEntityCollectionReference<>(clazz, entityIds);
		runNonConsuming(String.format("class %s", clazz), entityService.getQuery(reference), batchRunnable);
	}

	/**
	 * Runs a batch execution against a {@link IQuery query}'s result, <strong>expecting that the execution
	 * won't change the query's results</strong>.
	 * <p>This last requirement allows us to safely break down the execution this way:
	 * first execute on <code>query.list(0, 100)</code>, then (if there was a result) <code>query.list(100, 100)</code>,
	 * and so on.
	 * <p>The <code>query</code> may be:
	 * <ul>
	 * <li>Your own implementation (of IQuery, or more particularly of ISearchQuery)
	 * <li>Retrieved from a DAO that used {@link Queries#fromQueryDsl(com.querydsl.core.support.FetchableQueryBase)} to
	 * adapt a QueryDSL query
	 * </ul>
	 */
	public <E extends GenericEntity<Long, ?>> void runNonConsuming(String loggerContext, final IQuery<E> query,
			final IBatchRunnable<E> batchRunnable) {
		doRun(loggerContext, query, false, batchRunnable);
	}

	/**
	 * Runs a batch execution against a {@link IQuery query}'s result, <strong>expecting that the execution
	 * will remove all processed elements from the query's results</strong>.
	 * <p>This last requirement allows us to safely break down the execution this way:
	 * first execute on <code>query.list(0, 100)</code>, then (if there was a result) execute on
	 * <code>query.list(0, 100)</code> <strong>again</strong>, and so on.
	 * <p>The <code>query</code> may be:
	 * <ul>
	 * <li>Your own implementation (of IQuery, or more particularly of ISearchQuery)
	 * <li>Retrieved from a DAO that used {@link Queries#fromQueryDsl(com.querydsl.core.support.FetchableQueryBase)} to
	 * adapt a QueryDSL query
	 * </ul>
	 */
	public <E extends GenericEntity<Long, ?>> void runConsuming(String loggerContext, final IQuery<E> query,
			final IBatchRunnable<E> batchRunnable) {
		doRun(loggerContext, query, true, batchRunnable);
	}

	private <E extends GenericEntity<Long, ?>> void doRun(String loggerContext, final IQuery<E> query, boolean consuming,
			final IBatchRunnable<E> batchRunnable) {
		long totalCount = query.count();
		long offset = 0;
		LOGGER.info("Beginning batch for %1$s: %2$d objects", loggerContext, totalCount);
		
		try {
			LOGGER.info("    preExecute start");
			
			writeRequiredTransactionTemplate.execute(new TransactionCallback<Void>() {
				@Override
				public Void doInTransaction(TransactionStatus status) {
					batchRunnable.preExecute();
					return null;
				}
			});
			
			LOGGER.info("    preExecute end");
			
			LOGGER.info("    starting batch executions");
			
			List<E> entityPartition = query.list(0, batchSize);
			while (entityPartition.size() > 0) {
				final List<E> entityPartitionToExecute = entityPartition;
				writeRequiredTransactionTemplate.execute(new TransactionCallback<Void>() {
					@Override
					public Void doInTransaction(TransactionStatus status) {
						executePartition(batchRunnable, entityPartitionToExecute);
						return null;
					}
				});
				
				offset += entityPartition.size();
				LOGGER.info("        treated %1$d/%2$d objects", offset, totalCount);
				
				// Don't use the offset if the runnable is consuming the query's results
				entityPartition = query.list(consuming ? 0 : offset, batchSize);
			}
			
			LOGGER.info("    end of batch executions");
	
			LOGGER.info("    postExecute start");
			
			writeRequiredTransactionTemplate.execute(new TransactionCallback<Void>() {
				@Override
				public Void doInTransaction(TransactionStatus status) {
					batchRunnable.postExecute();
					return null;
				}
			});
			
			LOGGER.info("    postExecute end");
			
			if (classesToReindex.size() > 0) {
				LOGGER.info("    reindexing classes %1$s", Joiners.onComma().join(classesToReindex));
				try {
					hibernateSearchService.reindexClasses(classesToReindex);
				} catch (ServiceException e) {
					LOGGER.error("    reindexing failure", e);
				}
				LOGGER.info("    end of reindexing");
			}
			
			LOGGER.info("End of batch for %1$s: %2$d/%3$d objects treated", loggerContext, offset, totalCount);
		} catch (RuntimeException e) {
			LOGGER.info("End of batch for %1$s: %2$d/%3$d objects treated, but caught exception '%s'",
					loggerContext, offset, totalCount, e);
			try {
				LOGGER.info("    onError start");
				batchRunnable.onError(e);
				LOGGER.info("    onError end (exception was NOT re-thrown)");
			} finally {
				LOGGER.info("    onError end (exception WAS re-thrown)");
			}
		}
	}
	
	private <E> void executePartition(IBatchRunnable<E> batchRunnable, List<E> entityPartition) {
		batchRunnable.executePartition(entityPartition);
		
		entityService.flush();
		if (flushToIndexes) {
			hibernateSearchService.flushToIndexes();
		}
		entityService.clear();
	}

	protected <E extends GenericEntity<Long, ?>> List<E> listEntitiesByIds(Class<E> clazz, Collection<Long> entityIds) {
		return entityService.listEntity(clazz, entityIds);
	}

	@Override
	protected SimpleHibernateBatchExecutor thisAsT() {
		return this;
	}
	
}
