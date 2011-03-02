package fr.openwide.core.hibernate.security.business.person.dao;

import fr.openwide.core.hibernate.business.generic.dao.GenericEntityDao;
import fr.openwide.core.hibernate.security.business.person.model.CorePerson;

public interface PersonDao extends GenericEntityDao<Integer, CorePerson> {
	
	Long countActive();
	
}