package org.iglooproject.basicapp.core.business.user.search;

import java.util.List;

import org.apache.lucene.search.SortField;

import com.google.common.collect.ImmutableList;

import org.iglooproject.basicapp.core.business.user.model.UserGroup;
import org.iglooproject.jpa.business.generic.model.GenericEntity;
import org.iglooproject.jpa.more.business.sort.ISort;
import org.iglooproject.jpa.more.business.sort.SortUtils;

public enum UserGroupSort implements ISort<SortField> {
	NAME {
		@Override
		public List<SortField> getSortFields(SortOrder sortOrder) {
			return ImmutableList.of(
					SortUtils.luceneSortField(this, sortOrder, SortField.Type.STRING, UserGroup.NAME_SORT)
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
			return ImmutableList.of(SortUtils.luceneSortField(this, sortOrder, SortField.Type.LONG, GenericEntity.ID_SORT));
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
