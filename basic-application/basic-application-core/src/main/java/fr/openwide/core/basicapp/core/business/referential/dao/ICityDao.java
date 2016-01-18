package fr.openwide.core.basicapp.core.business.referential.dao;

import fr.openwide.core.basicapp.core.business.referential.model.City;
import fr.openwide.core.jpa.business.generic.dao.IGenericEntityDao;

public interface ICityDao extends IGenericEntityDao<Long, City> {

	City getByUniqueKeyIgnoreCase(String libelle);
}
