package org.iglooproject.jpa.security.business.user.util;

import static org.iglooproject.jpa.business.generic.model.GenericEntity.STRING_COLLATOR_FRENCH;

import org.iglooproject.jpa.business.generic.util.AbstractGenericEntityComparator;
import org.iglooproject.jpa.security.business.user.model.GenericUserGroup;

import com.google.common.collect.ComparisonChain;

public class GenericUserGroupComparator extends AbstractGenericEntityComparator<Long, GenericUserGroup<?, ?>> {

	private static final long serialVersionUID = -8477673271287138297L;

	public static final GenericUserGroupComparator INSTANCE = new GenericUserGroupComparator();

	public static GenericUserGroupComparator get() {
		return INSTANCE;
	}

	@Override
	protected int compareNotNullObjects(GenericUserGroup<?, ?> left, GenericUserGroup<?, ?> right) {
		int order = ComparisonChain.start()
			.compare(left.getName(), right.getName(), STRING_COLLATOR_FRENCH)
			.result();
		
		if (order == 0) {
			order = super.compareNotNullObjects(left, right);
		}
		
		return order;
	}

}
