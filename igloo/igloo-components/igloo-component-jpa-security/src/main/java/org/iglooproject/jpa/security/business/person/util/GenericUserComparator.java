package org.iglooproject.jpa.security.business.person.util;

import org.iglooproject.jpa.business.generic.util.AbstractGenericEntityComparator;
import org.iglooproject.jpa.security.business.person.model.GenericUser;

public class GenericUserComparator extends AbstractGenericEntityComparator<Long, GenericUser<?, ?>> {

	private static final long serialVersionUID = 5465095061690808293L;

	private static final GenericUserComparator INSTANCE = new GenericUserComparator();

	public static GenericUserComparator get() {
		return INSTANCE;
	}

	@Override
	protected int compareNotNullObjects(GenericUser<?, ?> left, GenericUser<?, ?> right) {
		@SuppressWarnings({ "unchecked", "rawtypes" }) // Hack allowing applications to override user order. Will work as expected as long as there is only one user type.
		int order = ((GenericUser) left).compareTo((GenericUser) right);
		if (order == 0) {
			order = super.compareNotNullObjects(left, right);
		}
		return order;
	}

}
