package test.wicket.more.model;

import org.apache.wicket.model.IModel;
import org.iglooproject.wicket.more.model.GenericEntityModel;

import test.wicket.more.business.person.model.Person;
import test.wicket.more.config.spring.SpringBootTestWicketSimple;

@SpringBootTestWicketSimple
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
