package org.iglooproject.test.business.person.service;

import org.iglooproject.jpa.business.generic.service.GenericEntityServiceImpl;
import org.iglooproject.test.business.person.dao.IPersonReferenceDao;
import org.iglooproject.test.business.person.model.PersonReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("personReferenceService")
public class PersonReferenceServiceImpl extends GenericEntityServiceImpl<Long, PersonReference> implements
		IPersonReferenceService {

	@Autowired
	public PersonReferenceServiceImpl(IPersonReferenceDao genericDao) {
		super(genericDao);
	}

}
