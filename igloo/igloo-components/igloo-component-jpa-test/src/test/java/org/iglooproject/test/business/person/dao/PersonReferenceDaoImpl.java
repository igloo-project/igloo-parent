package org.iglooproject.test.business.person.dao;

import org.iglooproject.jpa.business.generic.dao.GenericEntityDaoImpl;
import org.iglooproject.test.business.person.model.PersonReference;
import org.springframework.stereotype.Repository;

@Repository("personReferenceDao")
public class PersonReferenceDaoImpl extends GenericEntityDaoImpl<Long, PersonReference> implements IPersonReferenceDao {

}
