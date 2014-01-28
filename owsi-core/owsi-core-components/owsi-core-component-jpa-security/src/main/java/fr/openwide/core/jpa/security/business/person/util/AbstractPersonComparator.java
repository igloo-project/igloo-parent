package fr.openwide.core.jpa.security.business.person.util;

import fr.openwide.core.jpa.business.generic.model.GenericEntity;
import fr.openwide.core.jpa.business.generic.util.AbstractGenericEntityComparator;
import fr.openwide.core.jpa.security.business.person.model.AbstractPerson;

public class AbstractPersonComparator extends AbstractGenericEntityComparator<Long, AbstractPerson<?>> {

	private static final long serialVersionUID = 5465095061690808293L;
	
	private static final AbstractPersonComparator INSTANCE = new AbstractPersonComparator();
	
	public static AbstractPersonComparator get() {
		return INSTANCE;
	}

	@Override
	protected int compareNotNullObjects(AbstractPerson<?> left, AbstractPerson<?> right) {
		int order = GenericEntity.DEFAULT_STRING_COLLATOR.compare(left.getLastName(), right.getLastName());
		if (order == 0) {
			order = GenericEntity.DEFAULT_STRING_COLLATOR.compare(left.getFirstName(), right.getFirstName());
		}
		if (order == 0) {
			order = super.compareNotNullObjects(left, right);
		}
		return order;
	}
	
}
