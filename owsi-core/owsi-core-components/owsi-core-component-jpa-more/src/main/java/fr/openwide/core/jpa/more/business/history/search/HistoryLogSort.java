package fr.openwide.core.jpa.more.business.history.search;

import java.util.List;

import org.apache.lucene.search.SortField;

import com.google.common.collect.ImmutableList;

import fr.openwide.core.jpa.business.generic.model.GenericEntity;
import fr.openwide.core.jpa.more.business.history.model.AbstractHistoryLog;
import fr.openwide.core.jpa.more.business.sort.ISort;
import fr.openwide.core.jpa.more.business.sort.SortUtils;

public enum HistoryLogSort implements ISort<SortField> {
	
	ID {
		@Override
		public List<SortField> getSortFields(SortOrder sortOrder) {
			return ImmutableList.of(
					SortUtils.luceneSortField(
							this, sortOrder, SortField.Type.LONG,
							GenericEntity.ID_SORT
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
			return ImmutableList.of(
					SortUtils.luceneSortField(
							this, sortOrder, SortField.Type.LONG,
							AbstractHistoryLog.DATE
					),
					SortUtils.luceneSortField( // L'ID donne une bonne clé de substitution pour le tri par date
							this, sortOrder, SortField.Type.LONG,
							GenericEntity.ID_SORT
					)
			);
		}
		@Override
		public SortOrder getDefaultOrder() {
			return SortOrder.DESC;
		}
	};
	
	@Override
	public abstract List<SortField> getSortFields(SortOrder sortOrder);
	
	@Override
	public abstract SortOrder getDefaultOrder();

}
