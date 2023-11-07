package org.iglooproject.jpa.security.business.user.util;

import static org.iglooproject.jpa.business.generic.model.GenericEntity.STRING_COLLATOR_FRENCH;

import org.iglooproject.jpa.business.generic.model.GenericEntity;
import org.iglooproject.jpa.business.generic.util.AbstractGenericEntityComparator;
import org.iglooproject.jpa.security.business.user.model.GenericUser;

import com.google.common.collect.ComparisonChain;

public class GenericUserComparator extends AbstractGenericEntityComparator<Long, GenericUser<?, ?>> {

	private static final long serialVersionUID = 5465095061690808293L;

	private static final GenericUserComparator INSTANCE = new GenericUserComparator();

	public static GenericUserComparator get() {
		return INSTANCE;
	}

	@Override
	protected int compareNotNullObjects(GenericUser<?, ?> left, GenericUser<?, ?> right) {
		int order = ComparisonChain.start()
			.compare(left.getUsername(), right.getUsername(), STRING_COLLATOR_FRENCH)
			.result();
		
		if (order == 0) {
			order = super.compareNotNullObjects(left, right);
		}
		
		return order;
	}

}
