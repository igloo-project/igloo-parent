package test.wicket.more.business.person.dao;

import org.iglooproject.jpa.business.generic.dao.GenericEntityDaoImpl;
import org.springframework.stereotype.Repository;
import test.wicket.more.business.person.model.Person;

@Repository("personDao")
public class PersonDaoImpl extends GenericEntityDaoImpl<Long, Person> implements IPersonDao {

  public PersonDaoImpl() {
    super();
  }
}
