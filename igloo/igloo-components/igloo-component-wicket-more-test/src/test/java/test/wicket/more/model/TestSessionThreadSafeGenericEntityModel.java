package test.wicket.more.model;

import org.apache.wicket.model.IModel;
import org.iglooproject.wicket.more.model.threadsafe.SessionThreadSafeGenericEntityModel;
import test.wicket.more.business.person.model.Person;

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
