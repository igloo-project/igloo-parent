package org.iglooproject.test.rest.jersey2.business.person.dao;

import org.iglooproject.jpa.business.generic.dao.GenericEntityDaoImpl;
import org.iglooproject.test.rest.jersey2.business.person.model.Person;
import org.springframework.stereotype.Repository;

@Repository("personDao")
public class PersonDaoImpl extends GenericEntityDaoImpl<Long, Person> implements IPersonDao {

  public PersonDaoImpl() {
    super();
  }
}
