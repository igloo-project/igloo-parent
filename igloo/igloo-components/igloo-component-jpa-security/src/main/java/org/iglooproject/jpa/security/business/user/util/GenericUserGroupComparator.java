package org.iglooproject.jpa.security.business.user.util;

import org.iglooproject.jpa.business.generic.model.GenericEntity;
import org.iglooproject.jpa.business.generic.util.AbstractGenericEntityComparator;
import org.iglooproject.jpa.security.business.user.model.GenericUserGroup;

public class GenericUserGroupComparator extends AbstractGenericEntityComparator<Long, GenericUserGroup<?, ?>> {

	private static final long serialVersionUID = -8477673271287138297L;

	public static final GenericUserGroupComparator INSTANCE = new GenericUserGroupComparator();

	public static GenericUserGroupComparator get() {
		return INSTANCE;
	}

	@Override
	protected int compareNotNullObjects(GenericUserGroup<?, ?> left, GenericUserGroup<?, ?> right) {
		int order = GenericEntity.STRING_COLLATOR_FRENCH.compare(left.getName(), right.getName());
		if (order == 0) {
			order = super.compareNotNullObjects(left, right);
		}
		return order;
	}

}
