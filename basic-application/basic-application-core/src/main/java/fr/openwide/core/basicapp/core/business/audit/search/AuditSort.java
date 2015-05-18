package fr.openwide.core.basicapp.core.business.audit.search;

import java.util.List;

import org.apache.lucene.search.SortField;

import com.google.common.collect.ImmutableList;

import fr.openwide.core.basicapp.core.business.audit.model.Audit;
import fr.openwide.core.basicapp.core.util.binding.Bindings;
import fr.openwide.core.jpa.more.business.sort.ISort;
import fr.openwide.core.jpa.more.business.sort.SortUtils;

public enum AuditSort implements ISort<SortField> {
	DATE_AND_ID_DESC {
		@Override
		public List<SortField> getSortFields(SortOrder sortOrder) {
			return ImmutableList.of(
					SortUtils.luceneSortField(this, sortOrder, SortField.Type.STRING, Audit.DATE_SORT_FIELD_NAME),
					SortUtils.luceneSortField(this, sortOrder, SortField.Type.LONG, Bindings.audit().id().getPath())
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