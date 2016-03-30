package fr.openwide.test.core.rest.jersey2.business.person.dao;

import org.springframework.stereotype.Repository;

import fr.openwide.core.jpa.business.generic.dao.GenericEntityDaoImpl;
import fr.openwide.test.core.rest.jersey2.business.person.model.Person;

@Repository("personDao")
public class PersonDaoImpl extends GenericEntityDaoImpl<Long, Person> implements IPersonDao {

	public PersonDaoImpl() {
		super();
	}
}
