package fr.openwide.core.jpa.more.business.task.dao;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.queryParser.QueryParser.Operator;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.hibernate.search.Environment;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.FullTextQuery;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.DatabaseRetrievalMethod;
import org.hibernate.search.query.ObjectLookupMethod;
import org.hibernate.search.query.dsl.BooleanJunction;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.mysema.query.jpa.JPQLQuery;
import com.mysema.query.jpa.impl.JPAQuery;

import fr.openwide.core.jpa.business.generic.dao.GenericEntityDaoImpl;
import fr.openwide.core.jpa.exception.ServiceException;
import fr.openwide.core.jpa.more.business.task.model.QQueuedTaskHolder;
import fr.openwide.core.jpa.more.business.task.model.QueuedTaskHolder;
import fr.openwide.core.jpa.more.business.task.model.QueuedTaskHolderBinding;
import fr.openwide.core.jpa.more.business.task.search.QueuedTaskHolderSearchQueryParameters;
import fr.openwide.core.jpa.more.business.task.util.TaskStatus;
import fr.openwide.core.spring.util.StringUtils;

public class QueuedTaskHolderDaoImpl extends GenericEntityDaoImpl<Long, QueuedTaskHolder>
		implements IQueuedTaskHolderDao {

	private static final Logger LOGGER = LoggerFactory.getLogger(QueuedTaskHolderDaoImpl.class);
	
	private static final QQueuedTaskHolder qQueuedTaskHolder = QQueuedTaskHolder.queuedTaskHolder; // NOSONAR
	
	private static final QueuedTaskHolderBinding QUEUED_TASK_HOLDER_BINDING = new QueuedTaskHolderBinding();

	@SuppressWarnings("unchecked")
	@Override
	public List<QueuedTaskHolder> search(QueuedTaskHolderSearchQueryParameters searchParams, Long limit, Long offset)
			throws ServiceException {
		try {
			// Request building
			FullTextQuery fullTextQuery = getQueuedTaskHolderQuery(searchParams);

			// Pagination
			if (offset != null) {
				fullTextQuery.setFirstResult(offset.intValue());
			}
			if (limit != null) {
				fullTextQuery.setMaxResults(limit.intValue());
			}

			// Sort
			List<SortField> sortFields = Lists.newArrayList();

			sortFields.add(new SortField(QUEUED_TASK_HOLDER_BINDING.endDate().getPath(), SortField.STRING, true));
			sortFields.add(new SortField(QueuedTaskHolder.NAME_SORT_FIELD_NAME, SortField.STRING));
			sortFields.add(new SortField(QUEUED_TASK_HOLDER_BINDING.id().getPath(), SortField.LONG));

			fullTextQuery.setSort(new Sort(sortFields.toArray(new SortField[sortFields.size()])));
			fullTextQuery.initializeObjectsWith(ObjectLookupMethod.SECOND_LEVEL_CACHE, DatabaseRetrievalMethod.QUERY);

			return fullTextQuery.getResultList();
		} catch (Exception e) {
			throw new ServiceException("Error while searching tasks", e);
		}
	}

	@Override
	public int count(QueuedTaskHolderSearchQueryParameters searchParams) throws ServiceException {
		try {
			FullTextQuery fullTextQuery = getQueuedTaskHolderQuery(searchParams);
			return fullTextQuery.getResultSize();
		} catch (Exception e) {
			throw new ServiceException("Error while counting tasks", e);
		}
	}

	private FullTextQuery getQueuedTaskHolderQuery(QueuedTaskHolderSearchQueryParameters searchParams)
			throws ServiceException {
		FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(getEntityManager());

		Analyzer analyzer = fullTextEntityManager.getSearchFactory().getAnalyzer(QueuedTaskHolder.class);
		
		QueryBuilder queryBuilder = fullTextEntityManager.getSearchFactory().buildQueryBuilder()
				.forEntity(QueuedTaskHolder.class).get();

		BooleanJunction<?> booleanJunction = queryBuilder.bool().must(queryBuilder.all().createQuery());

		String name = searchParams.getName();
		if (StringUtils.hasText(name)) {
			QueryParser parser = new QueryParser(Environment.DEFAULT_LUCENE_MATCH_VERSION,
					QUEUED_TASK_HOLDER_BINDING.name().getPath(),
					analyzer
					);
			parser.setDefaultOperator(Operator.AND);
			try {
				booleanJunction.must(parser.parse(QueryParser.escape(name)));
			} catch (ParseException e) {
				LOGGER.error("Error while parsing", e);
			}
		}

		List<TaskStatus> statuses = searchParams.getStatuses();
		if (statuses != null && !statuses.isEmpty()) {
			BooleanJunction<?> subJunction = queryBuilder.bool();

			for (TaskStatus status : statuses) {
				subJunction.should(queryBuilder.keyword().onField(QUEUED_TASK_HOLDER_BINDING.status().getPath())
						.matching(status).createQuery());
			}

			booleanJunction.must(subJunction.createQuery());
		}

		List<String> tasktypes = searchParams.getTaskTypes();
		if (tasktypes != null && !tasktypes.isEmpty()) {
			BooleanJunction<?> subJunction = queryBuilder.bool();

			for (String taskType : tasktypes) {
				subJunction.should(queryBuilder.keyword().onField(QUEUED_TASK_HOLDER_BINDING.taskType().getPath())
						.matching(taskType).createQuery());
			}

			booleanJunction.must(subJunction.createQuery());
		}

		Date creationDate = searchParams.getCreationDate();
		if (creationDate != null) {
			booleanJunction.must(queryBuilder.range().onField(QUEUED_TASK_HOLDER_BINDING.creationDate().getPath())
					.below(creationDate).createQuery());
		}

		Date startDate = searchParams.getStartDate();
		if (creationDate != null) {
			booleanJunction.must(queryBuilder.range().onField(QUEUED_TASK_HOLDER_BINDING.startDate().getPath())
					.below(startDate).createQuery());
		}

		Date endDate = searchParams.getEndDate();
		if (creationDate != null) {
			booleanJunction.must(queryBuilder.range().onField(QUEUED_TASK_HOLDER_BINDING.endDate().getPath())
					.below(endDate).createQuery());
		}

		return fullTextEntityManager.createFullTextQuery(booleanJunction.createQuery(), QueuedTaskHolder.class);
	}

	@Override
	public Long count(Date since, TaskStatus... statuses) {
		JPQLQuery query = new JPAQuery(getEntityManager());

		query.from(qQueuedTaskHolder).where(
				qQueuedTaskHolder.status.in(statuses).and(qQueuedTaskHolder.creationDate.after(since)));

		return query.count();
	}
	
	@Override
	public Long count(TaskStatus... statuses) {
		JPQLQuery query = new JPAQuery(getEntityManager());

		query.from(qQueuedTaskHolder).where(qQueuedTaskHolder.status.in(statuses));

		return query.count();
	}
	
	@Override
	public QueuedTaskHolder getNextTaskForExecution(String taskType) {
		Date now = new Date();
		
		QQueuedTaskHolder qQueuedTask = QQueuedTaskHolder.queuedTaskHolder;
		
		JPQLQuery query = new JPAQuery(getEntityManager());
		
		query.from(qQueuedTask)
				.where(qQueuedTask.taskType.eq(taskType)
						.and(qQueuedTask.startDate.isNull())
						.and(qQueuedTask.triggeringDate.isNull()
								.or(qQueuedTask.triggeringDate.before(now))))
				.orderBy(qQueuedTask.version.asc(), qQueuedTask.creationDate.asc());
		
		List<QueuedTaskHolder> tasksForExecution = query.list(qQueuedTask);
		
		if (tasksForExecution.isEmpty()) {
			return null;
		} else {
			return tasksForExecution.get(0);
		}
	}

	@Override
	public QueuedTaskHolder getStalledTask(String taskType, int executionTimeLimitInSeconds) {
		Calendar timeLimit = Calendar.getInstance();
		timeLimit.add(Calendar.SECOND, -executionTimeLimitInSeconds);
		
		QQueuedTaskHolder qQueuedTask = QQueuedTaskHolder.queuedTaskHolder;
		
		JPQLQuery query = new JPAQuery(getEntityManager());
		
		query.from(qQueuedTask)
				.where(qQueuedTask.startDate.isNotNull()
						.and(qQueuedTask.taskType.eq(taskType))
						.and(qQueuedTask.startDate.before(timeLimit.getTime())));
		
		List<QueuedTaskHolder> stalledTasks = query.list(qQueuedTask);
		
		if (stalledTasks.isEmpty()) {
			return null;
		} else {
			return stalledTasks.get(0);
		}
	}
	
	@Override
	public List<QueuedTaskHolder> listConsumable() {
		JPQLQuery query = new JPAQuery(getEntityManager());

		query.from(qQueuedTaskHolder)
				.where(qQueuedTaskHolder.status.in(TaskStatus.CONSUMABLE_TASK_STATUS))
				.orderBy(qQueuedTaskHolder.id.asc());

		return query.list(qQueuedTaskHolder);
	}

	@Override
	public List<String> listTypes() {
		JPQLQuery query = new JPAQuery(getEntityManager());

		query.from(qQueuedTaskHolder).orderBy(qQueuedTaskHolder.taskType.asc()).distinct();

		return query.list(qQueuedTaskHolder.taskType);
	}
}
