package basicapp.back.business.referencedata.search;

import static org.iglooproject.jpa.more.search.query.HibernateSearchUtils.toSortOrder;

import java.util.List;
import java.util.function.Function;

import org.hibernate.search.engine.search.sort.dsl.SearchSortFactory;
import org.hibernate.search.engine.search.sort.dsl.SortFinalStep;
import org.iglooproject.jpa.more.business.sort.ISort;

import basicapp.back.business.referencedata.model.City;

public enum CitySort implements ISort<Function<SearchSortFactory, SortFinalStep>> {

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
				f -> f.field(City.ID).order(toSortOrder(this, sortOrder))
			);
		}
		@Override
		public SortOrder getDefaultOrder() {
			return SortOrder.DESC;
		}
	},
	POSITION {
		@Override
		public List<Function<SearchSortFactory, SortFinalStep>> getSortFields(SortOrder sortOrder) {
			return List.of(
				f -> f.field(City.POSITION).order(toSortOrder(this, sortOrder))
			);
		}
		@Override
		public SortOrder getDefaultOrder() {
			return SortOrder.ASC;
		}
	},
	LABEL_FR {
		@Override
		public List<Function<SearchSortFactory, SortFinalStep>> getSortFields(SortOrder sortOrder) {
			return List.of(
				f -> f.field(City.LABEL_FR_SORT).order(toSortOrder(this, sortOrder))
			);
		}
		@Override
		public SortOrder getDefaultOrder() {
			return SortOrder.ASC;
		}
	},
	LABEL_EN {
		@Override
		public List<Function<SearchSortFactory, SortFinalStep>> getSortFields(SortOrder sortOrder) {
			return List.of(
				f -> f.field(City.LABEL_EN_SORT).order(toSortOrder(this, sortOrder))
			);
		}
		@Override
		public SortOrder getDefaultOrder() {
			return SortOrder.ASC;
		}
	},
	POSTAL_CODE {
		@Override
		public List<Function<SearchSortFactory, SortFinalStep>> getSortFields(SortOrder sortOrder) {
			return List.of(
				f -> f.field(City.POSTAL_CODE).order(toSortOrder(this, sortOrder))
			);
		}
		@Override
		public SortOrder getDefaultOrder() {
			return SortOrder.ASC;
		}
	},;

}
