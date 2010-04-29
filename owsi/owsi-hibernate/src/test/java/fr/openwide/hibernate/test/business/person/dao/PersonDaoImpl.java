package fr.openwide.hibernate.test.business.person.dao;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import fr.openwide.hibernate.business.generic.dao.GenericEntityDaoImpl;
import fr.openwide.hibernate.test.business.person.model.Person;

@Repository("personDao")
public class PersonDaoImpl extends GenericEntityDaoImpl<Person> implements PersonDao {

	@Autowired
	public PersonDaoImpl(SessionFactory sessionFactory) {
		super(sessionFactory);
	}
}
