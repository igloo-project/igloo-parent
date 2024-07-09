package basicapp.back.business.user.search;


import static org.iglooproject.jpa.more.search.query.HibernateSearchUtils.toSortOrder;

import java.util.List;
import java.util.function.Function;

import org.hibernate.search.engine.search.sort.dsl.SearchSortFactory;
import org.hibernate.search.engine.search.sort.dsl.SortFinalStep;
import org.iglooproject.jpa.more.business.sort.ISort;

import basicapp.back.business.user.model.UserGroup;

public enum UserGroupSort implements ISort<Function<SearchSortFactory, SortFinalStep>> {

	SCORE {
		@Override
		public List<Function<SearchSortFactory, SortFinalStep>> getSortFields(SortOrder sortOrder) {
			return List.of(
				f -> f.score()
			);
		}
		@Override
		public SortOrder getDefaultOrder() {
			return SortOrder.DESC;
		}
	},
	ID {
		@Override
		public List<Function<SearchSortFactory, SortFinalStep>> getSortFields(SortOrder sortOrder) {
			return List.of(
				f -> f.field(UserGroup.ID).order(toSortOrder(this, sortOrder))
			);
		}
		@Override
		public SortOrder getDefaultOrder() {
			return SortOrder.DESC;
		}
	},
	NAME {
		@Override
		public List<Function<SearchSortFactory, SortFinalStep>> getSortFields(SortOrder sortOrder) {
			return List.of(
				f -> f.field(UserGroup.NAME_SORT).order(toSortOrder(this, sortOrder))
			);
		}
		@Override
		public SortOrder getDefaultOrder() {
			return SortOrder.ASC;
		}
	};

}
