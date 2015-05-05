package fr.openwide.core.jpa.more.business.task.search;

import java.util.List;

import org.apache.lucene.search.SortField;

import com.google.common.collect.ImmutableList;

import fr.openwide.core.jpa.more.business.sort.ISort;
import fr.openwide.core.jpa.more.business.sort.SortUtils;
import fr.openwide.core.jpa.more.business.task.model.QueuedTaskHolder;
import fr.openwide.core.jpa.more.util.binding.CoreJpaMoreBindings;

public enum QueuedTaskHolderSort implements ISort<SortField> {
	
	CREATION_DATE {
		@Override
		public List<SortField> getSortFields(SortOrder sortOrder) {
			return ImmutableList.of(
					SortUtils.luceneStringSortField(this, sortOrder, QueuedTaskHolder.END_DATE_SORT_FIELD_NAME, NullSortValue.GREATEST),
					SortUtils.luceneStringSortField(this, sortOrder, QueuedTaskHolder.START_DATE_SORT_FIELD_NAME, NullSortValue.GREATEST),
					SortUtils.luceneSortField(this, sortOrder, SortField.Type.STRING, QueuedTaskHolder.CREATION_DATE_SORT_FIELD_NAME)
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
					SortUtils.luceneSortField(this, sortOrder, SortField.Type.STRING, QueuedTaskHolder.NAME_SORT_FIELD_NAME)
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
					SortUtils.luceneSortField(this, sortOrder, SortField.Type.LONG, CoreJpaMoreBindings.queuedTaskHolder().id())
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
