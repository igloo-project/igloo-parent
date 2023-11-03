package org.iglooproject.wicket.more.console.maintenance.task.model;

import java.util.function.UnaryOperator;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.util.CollectionModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.iglooproject.jpa.more.business.task.model.QueuedTaskHolder;
import org.iglooproject.jpa.more.business.task.search.IQueuedTaskHolderSearchQuery;
import org.iglooproject.jpa.more.business.task.search.QueuedTaskHolderSearchQueryData;
import org.iglooproject.jpa.more.business.task.search.QueuedTaskHolderSort;
import org.iglooproject.jpa.more.util.binding.CoreJpaMoreBindings;
import org.iglooproject.wicket.more.markup.html.sort.model.CompositeSortModel;
import org.iglooproject.wicket.more.markup.html.sort.model.CompositeSortModel.CompositingStrategy;
import org.iglooproject.wicket.more.model.data.DataModel;
import org.iglooproject.wicket.more.model.search.query.SearchQueryDataProvider;

import com.google.common.collect.ImmutableMap;

public class QueuedTaskHolderDataProvider extends SearchQueryDataProvider<QueuedTaskHolder, QueuedTaskHolderSort, QueuedTaskHolderSearchQueryData, IQueuedTaskHolderSearchQuery> {

	private static final long serialVersionUID = 1L;

	@SpringBean
	private IQueuedTaskHolderSearchQuery searchQuery;

	private final CompositeSortModel<QueuedTaskHolderSort> sortModel = new CompositeSortModel<>(
		CompositingStrategy.LAST_ONLY,
		ImmutableMap.of(
			QueuedTaskHolderSort.CREATION_DATE, QueuedTaskHolderSort.CREATION_DATE.getDefaultOrder()
		),
		ImmutableMap.of(
			QueuedTaskHolderSort.CREATION_DATE, QueuedTaskHolderSort.CREATION_DATE.getDefaultOrder(),
			QueuedTaskHolderSort.ID, QueuedTaskHolderSort.ID.getDefaultOrder()
		)
	);

	public QueuedTaskHolderDataProvider() {
		this(UnaryOperator.identity());
	}

	public QueuedTaskHolderDataProvider(UnaryOperator<DataModel<QueuedTaskHolderSearchQueryData>> dataModelOperator) {
		this(
			dataModelOperator.apply(
				new DataModel<>(QueuedTaskHolderSearchQueryData::new)
					.bind(CoreJpaMoreBindings.queuedTaskHolderSearchQueryData().name(), Model.of())
					.bind(CoreJpaMoreBindings.queuedTaskHolderSearchQueryData().statuses(), new CollectionModel<>())
					.bind(CoreJpaMoreBindings.queuedTaskHolderSearchQueryData().results(), new CollectionModel<>())
					.bind(CoreJpaMoreBindings.queuedTaskHolderSearchQueryData().taskTypes(), new CollectionModel<>())
					.bind(CoreJpaMoreBindings.queuedTaskHolderSearchQueryData().queueIds(), new CollectionModel<>())
					.bind(CoreJpaMoreBindings.queuedTaskHolderSearchQueryData().creationDate(), Model.of())
					.bind(CoreJpaMoreBindings.queuedTaskHolderSearchQueryData().startDate(), Model.of())
					.bind(CoreJpaMoreBindings.queuedTaskHolderSearchQueryData().endDate(), Model.of())
			)
		);
	}

	public QueuedTaskHolderDataProvider(IModel<QueuedTaskHolderSearchQueryData> dataModel) {
		super(dataModel);
	}

	@Override
	public CompositeSortModel<QueuedTaskHolderSort> getSortModel() {
		return sortModel;
	}

	@Override
	protected IQueuedTaskHolderSearchQuery searchQuery() {
		return searchQuery;
	}

}
