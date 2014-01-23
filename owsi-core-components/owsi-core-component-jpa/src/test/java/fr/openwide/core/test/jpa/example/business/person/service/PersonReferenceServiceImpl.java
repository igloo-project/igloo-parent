package fr.openwide.core.test.jpa.example.business.person.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.openwide.core.jpa.business.generic.service.GenericEntityServiceImpl;
import fr.openwide.core.test.jpa.example.business.person.dao.PersonReferenceDao;
import fr.openwide.core.test.jpa.example.business.person.model.PersonReference;

@Service("personReferenceService")
public class PersonReferenceServiceImpl extends GenericEntityServiceImpl<Long, PersonReference> implements
		PersonReferenceService {

	@Autowired
	public PersonReferenceServiceImpl(PersonReferenceDao genericDao) {
		super(genericDao);
	}

}
