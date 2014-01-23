package fr.openwide.core.test.jpa.example.business.person.dao;

import org.springframework.stereotype.Repository;

import fr.openwide.core.jpa.business.generic.dao.GenericEntityDaoImpl;
import fr.openwide.core.test.jpa.example.business.person.model.PersonReference;

@Repository("personReferenceDao")
public class PersonReferenceDaoImpl extends GenericEntityDaoImpl<Long, PersonReference> implements PersonReferenceDao {

}
