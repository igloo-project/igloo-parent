package org.iglooproject.basicapp.core.business.user.search;

import java.util.List;

import org.apache.lucene.search.SortField;
import org.iglooproject.basicapp.core.business.user.model.User;
import org.iglooproject.jpa.more.business.sort.ISort;
import org.iglooproject.jpa.more.business.sort.SortUtils;

import com.google.common.collect.ImmutableList;

public enum UserSort implements ISort<SortField> {

	LAST_NAME {
		@Override
		public List<SortField> getSortFields(SortOrder sortOrder) {
			return ImmutableList.of(
				SortUtils.luceneSortField(this, sortOrder, SortField.Type.STRING, User.LAST_NAME_SORT)
			);
		}
		@Override
		public SortOrder getDefaultOrder() {
			return SortOrder.ASC;
		}
	},
	FIRST_NAME {
		@Override
		public List<SortField> getSortFields(SortOrder sortOrder) {
			return ImmutableList.of(
				SortUtils.luceneSortField(this, sortOrder, SortField.Type.STRING, User.FIRST_NAME_SORT)
			);
		}
		
		@Override
		public SortOrder getDefaultOrder() {
			return SortOrder.ASC;
		}
	},
	FULL_NAME {
		@Override
		public List<SortField> getSortFields(SortOrder sortOrder) {
			return ImmutableList.of(
				SortUtils.luceneSortField(this, sortOrder, SortField.Type.STRING, User.LAST_NAME_SORT),
				SortUtils.luceneSortField(this, sortOrder, SortField.Type.STRING, User.FIRST_NAME_SORT)
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
				SortUtils.luceneSortField(this, sortOrder, SortField.Type.LONG, User.ID)
			);
		}
		
		@Override
		public SortOrder getDefaultOrder() {
			return SortOrder.DESC;
		}
	};

}
