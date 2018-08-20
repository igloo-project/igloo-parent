package org.iglooproject.test.wicket.more.business.person.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.iglooproject.jpa.business.generic.service.GenericEntityServiceImpl;
import org.iglooproject.test.wicket.more.business.person.dao.IPersonDao;
import org.iglooproject.test.wicket.more.business.person.model.Person;

@Service
public class PersonServiceImpl extends GenericEntityServiceImpl<Long, Person>
		implements IPersonService {
	
	@Autowired
	public PersonServiceImpl(IPersonDao personDao) {
		super(personDao);
	}

}
