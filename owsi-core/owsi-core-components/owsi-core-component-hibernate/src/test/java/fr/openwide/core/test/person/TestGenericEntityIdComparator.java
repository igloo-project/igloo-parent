package fr.openwide.core.test.person;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import fr.openwide.core.hibernate.business.generic.util.GenericEntityIdComparator;
import fr.openwide.core.test.AbstractHibernateCoreTestCase;
import fr.openwide.core.test.hibernate.example.business.person.model.Person;

public class TestGenericEntityIdComparator extends
		AbstractHibernateCoreTestCase {

	@Test
	public void testCompare() {
		Person person1 = new Person("person1", "person1");
		person1.setId(1);
		
		Person person2 = new Person("person2", "person2");
		person2.setId(2);
		
		Person person3 = new Person("person3", "person3");
		person3.setId(3);
		
		Person person4 = new Person("person4", "person4");
		person4.setId(null);
		
		List<Person> personList = new ArrayList<Person>();
		personList.add(person2);
		personList.add(person4);
		personList.add(person1);
		personList.add(person3);
		
		Collections.sort(personList, GenericEntityIdComparator.INSTANCE);
		
		assertSame(person1, personList.get(0));
		assertSame(person2, personList.get(1));
		assertSame(person3, personList.get(2));
		assertSame(person4, personList.get(3));
	}
	
}
