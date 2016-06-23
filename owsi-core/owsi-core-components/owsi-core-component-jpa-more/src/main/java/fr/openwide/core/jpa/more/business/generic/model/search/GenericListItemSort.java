package fr.openwide.core.jpa.more.business.generic.model.search;

import java.util.List;

import org.apache.lucene.search.SortField;

import com.google.common.collect.ImmutableList;

import fr.openwide.core.jpa.more.business.generic.model.GenericListItem;
import fr.openwide.core.jpa.more.business.sort.ISort;
import fr.openwide.core.jpa.more.business.sort.SortUtils;

public enum GenericListItemSort implements ISort<SortField> {

	SCORE {
		@Override
		public List<SortField> getSortFields(SortOrder sortOrder) {
			return GenericEntitySort.SCORE.getSortFields(sortOrder);
		}
		@Override
		public SortOrder getDefaultOrder() {
			return GenericEntitySort.SCORE.getDefaultOrder();
		}
	},
	ID {
		@Override
		public List<SortField> getSortFields(SortOrder sortOrder) {
			return GenericEntitySort.ID.getSortFields(sortOrder);
		}
		@Override
		public SortOrder getDefaultOrder() {
			return GenericEntitySort.ID.getDefaultOrder();
		}
	},
	LABEL {
		@Override
		public List<SortField> getSortFields(SortOrder sortOrder) {
			return ImmutableList.of(
					SortUtils.luceneSortField(
							this, sortOrder, SortField.Type.STRING,
							GenericListItem.LABEL_SORT_FIELD_NAME
					)
			);
		}
		@Override
		public SortOrder getDefaultOrder() {
			return SortOrder.ASC;
		}
	},
	CODE {
		@Override
		public List<SortField> getSortFields(SortOrder sortOrder) {
			return ImmutableList.of(
					SortUtils.luceneSortField(
							this, sortOrder, SortField.Type.STRING,
							GenericListItem.CODE_SORT_FIELD_NAME
					)
			);
		}
		@Override
		public SortOrder getDefaultOrder() {
			return SortOrder.ASC;
		}
	},
	POSITION {
		@Override
		public List<SortField> getSortFields(SortOrder sortOrder) {
			return ImmutableList.of(
					SortUtils.luceneSortField(
							this, sortOrder, SortField.Type.INT,
							GenericListItem.POSITION_FIELD_NAME
					)
			);
		}
		@Override
		public SortOrder getDefaultOrder() {
			return SortOrder.ASC;
		}
	};
	
	@Override
	public abstract List<SortField> getSortFields(SortOrder sortOrder);
	
	@Override
	public abstract SortOrder getDefaultOrder();

}
