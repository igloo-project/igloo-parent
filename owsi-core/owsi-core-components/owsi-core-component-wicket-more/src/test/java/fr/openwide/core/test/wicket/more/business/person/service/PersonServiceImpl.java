package fr.openwide.core.test.wicket.more.business.person.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.openwide.core.jpa.business.generic.service.GenericEntityServiceImpl;
import fr.openwide.core.test.wicket.more.business.person.dao.IPersonDao;
import fr.openwide.core.test.wicket.more.business.person.model.Person;

@Service
public class PersonServiceImpl extends GenericEntityServiceImpl<Long, Person>
		implements IPersonService {
	
	@Autowired
	public PersonServiceImpl(IPersonDao personDao) {
		super(personDao);
	}

}
