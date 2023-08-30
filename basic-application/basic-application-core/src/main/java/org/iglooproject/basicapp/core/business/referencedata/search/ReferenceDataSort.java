package org.iglooproject.basicapp.core.business.referencedata.search;

import java.util.List;

import org.apache.lucene.search.SortField;
import org.iglooproject.basicapp.core.business.referencedata.model.ReferenceData;
import org.iglooproject.jpa.more.business.generic.model.search.GenericEntitySort;
import org.iglooproject.jpa.more.business.sort.ISort;
import org.iglooproject.jpa.more.business.sort.SortUtils;

import com.google.common.collect.ImmutableList;

public enum ReferenceDataSort implements ISort<SortField> {

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
	POSITION {
		@Override
		public List<SortField> getSortFields(SortOrder sortOrder) {
			return ImmutableList.of(
				SortUtils.luceneSortField(
					this, sortOrder, SortField.Type.INT,
					ReferenceData.POSITION
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
					ReferenceData.CODE
				)
			);
		}
		@Override
		public SortOrder getDefaultOrder() {
			return SortOrder.ASC;
		}
	},
	LABEL_FR {
		private static final long serialVersionUID = 1L;
		@Override
		public List<SortField> getSortFields(SortOrder sortOrder) {
			return ImmutableList.of(
				SortUtils.luceneSortField(
					this, sortOrder, SortField.Type.STRING,
					ReferenceData.LABEL_FR_SORT
				)
			);
		}
		@Override
		public SortOrder getDefaultOrder() {
			return SortOrder.ASC;
		}
	},
	LABEL_EN {
		private static final long serialVersionUID = 1L;
		@Override
		public List<SortField> getSortFields(SortOrder sortOrder) {
			return ImmutableList.of(
				SortUtils.luceneSortField(
					this, sortOrder, SortField.Type.STRING,
					ReferenceData.LABEL_EN_SORT
				)
			);
		}
		@Override
		public SortOrder getDefaultOrder() {
			return SortOrder.ASC;
		}
	};

}
