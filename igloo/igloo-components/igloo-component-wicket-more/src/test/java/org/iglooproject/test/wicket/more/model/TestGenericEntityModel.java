package org.iglooproject.test.wicket.more.model;

import org.apache.wicket.model.IModel;

import org.iglooproject.test.wicket.more.business.person.model.Person;
import org.iglooproject.wicket.more.model.GenericEntityModel;

public class TestGenericEntityModel extends AbstractTestGenericEntityModel {

	@Override
	protected IModel<Person> createModel() {
		return new GenericEntityModel<Long, Person>();
	}

	@Override
	protected IModel<Person> createModel(Person person) {
		return new GenericEntityModel<Long, Person>(person);
	}

}
