package fr.openwide.core.test.wicket.more.model;

import org.apache.wicket.model.IModel;

import fr.openwide.core.test.jpa.example.business.person.model.Person;
import fr.openwide.core.wicket.more.model.threadsafe.SessionThreadSafeGenericEntityModel;


public class TestSessionThreadSafeGenericEntityModel extends AbstractTestGenericEntityModel {

	@Override
	protected IModel<Person> createModel() {
		return new SessionThreadSafeGenericEntityModel<Long, Person>();
	}

	@Override
	protected IModel<Person> createModel(Person person) {
		return new SessionThreadSafeGenericEntityModel<Long, Person>(person);
	}

}
