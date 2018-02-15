package org.iglooproject.jpa.more.business.task.search;

import java.util.List;

import org.apache.lucene.search.SortField;
import org.iglooproject.jpa.business.generic.model.GenericEntity;
import org.iglooproject.jpa.more.business.sort.ISort;
import org.iglooproject.jpa.more.business.sort.SortUtils;
import org.iglooproject.jpa.more.business.task.model.QueuedTaskHolder;

import com.google.common.collect.ImmutableList;

public enum QueuedTaskHolderSort implements ISort<SortField> {
	
	CREATION_DATE {
		@Override
		public List<SortField> getSortFields(SortOrder sortOrder) {
			return ImmutableList.of(
					SortUtils.luceneLongSortField(this, sortOrder, QueuedTaskHolder.END_DATE_SORT, NullSortValue.GREATEST),
					SortUtils.luceneLongSortField(this, sortOrder, QueuedTaskHolder.START_DATE_SORT, NullSortValue.GREATEST),
					SortUtils.luceneSortField(this, sortOrder, SortField.Type.LONG, QueuedTaskHolder.CREATION_DATE_SORT)
			);
		}
		
		@Override
		public SortOrder getDefaultOrder() {
			return SortOrder.DESC;
		}
	},
	NAME {
		@Override
		public List<SortField> getSortFields(SortOrder sortOrder) {
			return ImmutableList.of(
					SortUtils.luceneSortField(this, sortOrder, SortField.Type.STRING, QueuedTaskHolder.NAME_SORT)
			);
		}
		
		@Override
		public SortOrder getDefaultOrder() {
			return SortOrder.ASC;
		}
	},
	ID {
		@Override
		public List<SortField> getSortFields(SortOrder sortOrder) {
			return ImmutableList.of(
					SortUtils.luceneSortField(this, sortOrder, SortField.Type.LONG, GenericEntity.ID_SORT)
			);
		}
		
		@Override
		public SortOrder getDefaultOrder() {
			return SortOrder.DESC;
		}
	};
	
	@Override
	public abstract SortOrder getDefaultOrder();
	
	@Override
	public abstract List<SortField> getSortFields(SortOrder sortOrder);
	
	// TODO RJO Sort : pourquoi on n'a pas ça dans l'interface ?
	public List<SortField> getSortFields() {
		return getSortFields(getDefaultOrder());
	}
}
