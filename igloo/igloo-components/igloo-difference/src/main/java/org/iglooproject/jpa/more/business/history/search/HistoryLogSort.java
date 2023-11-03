package org.iglooproject.jpa.more.business.history.search;

import static org.iglooproject.jpa.more.search.query.HibernateSearchUtils.toSortOrder;

import java.util.List;
import java.util.function.Function;

import org.hibernate.search.engine.search.sort.dsl.SearchSortFactory;
import org.hibernate.search.engine.search.sort.dsl.SortFinalStep;
import org.iglooproject.jpa.more.business.history.model.AbstractHistoryLog;
import org.iglooproject.jpa.more.business.sort.ISort;

public enum HistoryLogSort implements ISort<Function<SearchSortFactory, SortFinalStep>> {

	ID {
		@Override
		public List<Function<SearchSortFactory, SortFinalStep>> getSortFields(SortOrder sortOrder) {
			return List.of(
				f -> f.field(AbstractHistoryLog.ID).order(toSortOrder(this, sortOrder))
			);
		}
		@Override
		public SortOrder getDefaultOrder() {
			return SortOrder.ASC;
		}
	},
	DATE {
		@Override
		public List<Function<SearchSortFactory, SortFinalStep>> getSortFields(SortOrder sortOrder) {
			return List.of(
				f -> f.field(AbstractHistoryLog.DATE).order(toSortOrder(this, sortOrder))
			);
		}
		@Override
		public SortOrder getDefaultOrder() {
			return SortOrder.ASC;
		}
	};

}
