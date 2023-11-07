package test.wicket.more.business.person.model;

import static org.iglooproject.jpa.business.generic.model.GenericEntity.STRING_COLLATOR_FRENCH;

import org.iglooproject.jpa.business.generic.util.AbstractGenericEntityComparator;

import com.google.common.collect.ComparisonChain;

public class PersonComparator extends AbstractGenericEntityComparator<Long, Person> {

	private static final long serialVersionUID = 5465095061690808293L;

	private static final PersonComparator INSTANCE = new PersonComparator();

	public static PersonComparator get() {
		return INSTANCE;
	}

	@Override
	protected int compareNotNullObjects(Person left, Person right) {
		int order = ComparisonChain.start()
			.compare(left.getLastName(), right.getLastName(), STRING_COLLATOR_FRENCH)
			.compare(left.getFirstName(), right.getFirstName(), STRING_COLLATOR_FRENCH)
			.result();
		
		if (order == 0) {
			order = super.compareNotNullObjects(left, right);
		}
		
		return order;
	}

}
