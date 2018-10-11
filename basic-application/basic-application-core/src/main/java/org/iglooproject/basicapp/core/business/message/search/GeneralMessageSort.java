package org.iglooproject.basicapp.core.business.message.search;

import java.util.List;

import org.iglooproject.basicapp.core.business.message.model.QGeneralMessage;
import org.iglooproject.jpa.more.business.sort.ISort;
import org.iglooproject.jpa.more.business.sort.SortUtils;

import com.google.common.collect.ImmutableList;
import com.querydsl.core.types.OrderSpecifier;

public enum GeneralMessageSort implements ISort<OrderSpecifier<?>> {

	ID {
		@Override
		public List<OrderSpecifier<?>> getSortFields(SortOrder sortOrder) {
			return ImmutableList.of(
					SortUtils.orderSpecifier(this, sortOrder, QGeneralMessage.generalMessage.id)
			);
		}
		@Override
		public SortOrder getDefaultOrder() {
			return SortOrder.DESC;
		}
	},
	PUBLICATION_START_DATE_TIME {
		@Override
		public List<OrderSpecifier<?>> getSortFields(SortOrder sortOrder) {
			return ImmutableList.of(
					SortUtils.orderSpecifier(this, sortOrder, QGeneralMessage.generalMessage.publication.startDateTime)
			);
		}
		@Override
		public SortOrder getDefaultOrder() {
			return SortOrder.ASC;
		}
	};

	@Override
	public abstract List<OrderSpecifier<?>> getSortFields(SortOrder sortOrder);

	@Override
	public abstract SortOrder getDefaultOrder();

}
