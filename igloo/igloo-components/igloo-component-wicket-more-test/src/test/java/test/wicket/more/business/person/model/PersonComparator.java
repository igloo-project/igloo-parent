package test.wicket.more.business.person.model;

import org.iglooproject.jpa.business.generic.model.GenericEntity;
import org.iglooproject.jpa.business.generic.util.AbstractGenericEntityComparator;

import com.google.common.collect.Ordering;

public class PersonComparator extends AbstractGenericEntityComparator<Long, Person> {

	private static final long serialVersionUID = 5465095061690808293L;
	
	private static final PersonComparator INSTANCE = new PersonComparator();
	
	private static final Ordering<String> STRING_COMPARATOR = GenericEntity.STRING_COLLATOR_FRENCH;
	
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
