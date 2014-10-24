package fr.openwide.core.showcase.core.business.task.dao;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.time.DateUtils;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.FullTextQuery;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.DatabaseRetrievalMethod;
import org.hibernate.search.query.ObjectLookupMethod;
import org.hibernate.search.query.dsl.BooleanJunction;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import fr.openwide.core.jpa.business.generic.dao.GenericEntityDaoImpl;
import fr.openwide.core.jpa.exception.ServiceException;
import fr.openwide.core.jpa.more.business.sort.SortUtils;
import fr.openwide.core.jpa.more.business.task.model.QueuedTaskHolder;
import fr.openwide.core.jpa.more.business.task.search.QueuedTaskHolderSort;
import fr.openwide.core.jpa.more.business.task.util.TaskResult;
import fr.openwide.core.jpa.more.business.task.util.TaskStatus;
import fr.openwide.core.jpa.more.util.binding.CoreJpaMoreBindings;
import fr.openwide.core.showcase.core.business.task.model.ShowcaseTaskQueueId;
import fr.openwide.core.showcase.core.business.task.model.TaskTypeEnum;
import fr.openwide.core.showcase.core.business.task.model.search.TaskSearchQueryParameters;

@Repository("showcaseTaskDao")
public class ShowcaseTaskDaoImpl extends GenericEntityDaoImpl<Long, QueuedTaskHolder> implements
		IShowcaseTaskDao {

	@SuppressWarnings("unchecked")
	@Override
	public List<QueuedTaskHolder> search(TaskSearchQueryParameters searchParameters, Long limit, Long offset)
			throws ServiceException {
		try {
			FullTextQuery fullTextQuery = getSearchTaskQuery(searchParameters);
			if (offset != null) {
				fullTextQuery.setFirstResult(offset.intValue());
			}
			if (limit != null) {
				fullTextQuery.setMaxResults(limit.intValue());
			}
			
			fullTextQuery.setSort(SortUtils.getLuceneSortWithDefaults(
					searchParameters.getSort(), QueuedTaskHolderSort.ID)
			);
			fullTextQuery.initializeObjectsWith(ObjectLookupMethod.SECOND_LEVEL_CACHE, DatabaseRetrievalMethod.QUERY);
			
			return (List<QueuedTaskHolder>) fullTextQuery.getResultList();
		} catch (Exception e) {
			throw new ServiceException("Error while searching tasks.", e);
		}
	}

	@Override
	public long count(TaskSearchQueryParameters searchParameters) throws ServiceException {
		try {
			return getSearchTaskQuery(searchParameters).getResultSize();
		} catch (Exception e) {
			throw new ServiceException("Error while counting tasks.", e);
		}
	}

	private FullTextQuery getSearchTaskQuery(TaskSearchQueryParameters searchParameters) {
		FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(getEntityManager());
		
		QueryBuilder queryBuilder = fullTextEntityManager.getSearchFactory().buildQueryBuilder()
				.forEntity(QueuedTaskHolder.class).get();
		
		BooleanJunction<?> booleanJunction = queryBuilder.bool().must(queryBuilder.all().createQuery());
		
		ShowcaseTaskQueueId queueId = searchParameters.getQueueId();
		if (queueId != null) {
			booleanJunction.must(queryBuilder.keyword()
						.onField(CoreJpaMoreBindings.queuedTaskHolder().queueId().getPath())
						.matching(queueId.getUniqueStringId())
						.createQuery());
		}
		
		TaskTypeEnum type = searchParameters.getType();
		if (type != null) {
			booleanJunction.must(queryBuilder.keyword()
						.onField(CoreJpaMoreBindings.queuedTaskHolder().taskType().getPath())
						.matching(type.getTaskType())
						.createQuery());
		}
		
		String name = searchParameters.getName();
		if (StringUtils.hasText(name)) {
			booleanJunction.must(queryBuilder.keyword()
						.onField(CoreJpaMoreBindings.queuedTaskHolder().name().getPath())
						.matching(name)
						.createQuery());
		}
		
		TaskStatus status = searchParameters.getStatus();
		if (status != null) {
			booleanJunction.must(queryBuilder.keyword()
						.onField(CoreJpaMoreBindings.queuedTaskHolder().status().getPath())
						.matching(status)
						.createQuery());
		}
		
		TaskResult result = searchParameters.getResult();
		if (result != null) {
			booleanJunction.must(queryBuilder.keyword()
						.onField(CoreJpaMoreBindings.queuedTaskHolder().result().getPath())
						.matching(result)
						.createQuery());
		}
		
		Date dateMin = searchParameters.getDateMin();
		Date dateMax = searchParameters.getDateMax() != null ? DateUtils.addMilliseconds(DateUtils.addDays(searchParameters.getDateMax(), 1), -1) : null;
		if (dateMin != null && dateMax != null) {
			booleanJunction.must(queryBuilder.range()
					.onField(CoreJpaMoreBindings.queuedTaskHolder().startDate().getPath())
					.from(dateMin)
					.to(dateMax)
					.createQuery());
			booleanJunction.must(queryBuilder.range()
					.onField(CoreJpaMoreBindings.queuedTaskHolder().endDate().getPath())
					.from(dateMin)
					.to(dateMax)
					.createQuery());
		} else if (dateMin != null) {
			booleanJunction.must(queryBuilder.range()
					.onField(CoreJpaMoreBindings.queuedTaskHolder().startDate().getPath())
					.above(dateMin)
					.createQuery());
		} else if (dateMax != null) {
			booleanJunction.must(queryBuilder.range()
					.onField(CoreJpaMoreBindings.queuedTaskHolder().endDate().getPath())
					.below(dateMax)
					.createQuery());
		}
		
		return fullTextEntityManager.createFullTextQuery(booleanJunction.createQuery(), QueuedTaskHolder.class);
	}

}
