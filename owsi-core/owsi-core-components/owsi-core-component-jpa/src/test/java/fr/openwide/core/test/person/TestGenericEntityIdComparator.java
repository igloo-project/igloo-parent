package fr.openwide.core.test.person;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import fr.openwide.core.jpa.business.generic.util.GenericEntityComparator;
import fr.openwide.core.jpa.business.generic.util.GenericEntityIdComparator;
import fr.openwide.core.test.AbstractJpaCoreTestCase;
import fr.openwide.core.test.jpa.example.business.person.model.Person;

@SuppressWarnings("deprecation")
public class TestGenericEntityIdComparator extends
		AbstractJpaCoreTestCase {

	@Test
	public void testCompareOldComparator() {
		testCompare(GenericEntityIdComparator.INSTANCE);
	}
	
	@Test
	public void testCompareNewComparator() {
		testCompare(GenericEntityComparator.<Long, Person>get());
	}
	
	public void testCompare(Comparator<? super Person> comparator) {
		Person person1 = new Person("person1", "person1");
		person1.setId(1L);
		
		Person person2 = new Person("person2", "person2");
		person2.setId(2L);
		
		Person person3 = new Person("person3", "person3");
		person3.setId(3L);
		
		Person person4 = new Person("person4", "person4");
		person4.setId(null);
		
		List<Person> personList = new ArrayList<Person>();
		personList.add(person2);
		personList.add(person4);
		personList.add(person1);
		personList.add(person3);
		
		Collections.sort(personList, comparator);
		
		Assert.assertSame(person1, personList.get(0));
		Assert.assertSame(person2, personList.get(1));
		Assert.assertSame(person3, personList.get(2));
		Assert.assertSame(person4, personList.get(3));
	}
	
}
