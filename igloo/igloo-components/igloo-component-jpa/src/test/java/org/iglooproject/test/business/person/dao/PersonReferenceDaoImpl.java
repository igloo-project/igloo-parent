package org.iglooproject.test.business.person.dao;

import org.springframework.stereotype.Repository;

import org.iglooproject.jpa.business.generic.dao.GenericEntityDaoImpl;
import org.iglooproject.test.business.person.model.PersonReference;

@Repository("personReferenceDao")
public class PersonReferenceDaoImpl extends GenericEntityDaoImpl<Long, PersonReference> implements IPersonReferenceDao {

}
