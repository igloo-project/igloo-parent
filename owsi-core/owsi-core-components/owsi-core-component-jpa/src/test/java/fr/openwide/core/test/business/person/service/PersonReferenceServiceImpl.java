package fr.openwide.core.test.business.person.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.openwide.core.jpa.business.generic.service.GenericEntityServiceImpl;
import fr.openwide.core.test.business.person.dao.IPersonReferenceDao;
import fr.openwide.core.test.business.person.model.PersonReference;

@Service("personReferenceService")
public class PersonReferenceServiceImpl extends GenericEntityServiceImpl<Long, PersonReference> implements
		IPersonReferenceService {

	@Autowired
	public PersonReferenceServiceImpl(IPersonReferenceDao genericDao) {
		super(genericDao);
	}

}
