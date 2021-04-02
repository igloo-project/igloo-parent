package org.iglooproject.jpa.security.business.person.util;

import org.iglooproject.jpa.business.generic.model.GenericEntity;
import org.iglooproject.jpa.business.generic.util.AbstractGenericEntityComparator;
import org.iglooproject.jpa.security.business.person.model.GenericUserGroup;

public class AbstractPersonGroupComparator extends AbstractGenericEntityComparator<Long, GenericUserGroup<?, ?>> {

	private static final long serialVersionUID = -8477673271287138297L;

	public static final AbstractPersonGroupComparator INSTANCE = new AbstractPersonGroupComparator();

	public static AbstractPersonGroupComparator get() {
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
