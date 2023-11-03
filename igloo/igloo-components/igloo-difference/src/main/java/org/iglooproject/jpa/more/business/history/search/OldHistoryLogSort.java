package org.iglooproject.jpa.more.business.history.search;

import java.util.List;

import org.apache.lucene.search.SortField;
import org.iglooproject.jpa.business.generic.model.GenericEntity;
import org.iglooproject.jpa.more.business.history.model.AbstractHistoryLog;
import org.iglooproject.jpa.more.business.sort.ISort;
import org.iglooproject.jpa.more.business.sort.SortUtils;

@Deprecated
public enum OldHistoryLogSort implements ISort<SortField> {
	
	ID {
		@Override
		public List<SortField> getSortFields(SortOrder sortOrder) {
			return List.of(
				SortUtils.luceneSortField(
					this, sortOrder, SortField.Type.LONG,
					GenericEntity.ID
				)
			);
		}
		@Override
		public SortOrder getDefaultOrder() {
			return SortOrder.DESC;
		}
	},
	DATE {
		@Override
		public List<SortField> getSortFields(SortOrder sortOrder) {
			return List.of(
				SortUtils.luceneSortField(
					this, sortOrder, SortField.Type.LONG,
					AbstractHistoryLog.DATE
				),
				SortUtils.luceneSortField(
					this, sortOrder, SortField.Type.LONG,
					GenericEntity.ID
				)
			);
		}
		@Override
		public SortOrder getDefaultOrder() {
			return SortOrder.DESC;
		}
	};
	
}
