package fr.openwide.core.test.jpa.example.business.person.model;

import com.google.common.collect.Ordering;

import fr.openwide.core.jpa.business.generic.model.GenericEntity;
import fr.openwide.core.jpa.business.generic.util.AbstractGenericEntityComparator;

public class PersonComparator extends AbstractGenericEntityComparator<Long, Person> {

	private static final long serialVersionUID = 5465095061690808293L;
	
	private static final PersonComparator INSTANCE = new PersonComparator();
	
	private static final Ordering<String> STRING_COMPARATOR = GenericEntity.DEFAULT_STRING_COLLATOR;
	
	public static PersonComparator get() {
		return INSTANCE;
	}

	@Override
	protected int compareNotNullObjects(Person left, Person right) {
		int order = STRING_COMPARATOR.compare(left.getLastName(), right.getLastName());
		if (order == 0) {
			order = STRING_COMPARATOR.compare(left.getFirstName(), right.getFirstName());
		}
		if (order == 0) {
			order = super.compareNotNullObjects(left, right);
		}
		return order;
	}
	
}
