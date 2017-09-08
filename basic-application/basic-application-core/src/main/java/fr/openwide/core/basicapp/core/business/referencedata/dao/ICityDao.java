package fr.openwide.core.basicapp.core.business.referencedata.dao;

import fr.openwide.core.basicapp.core.business.common.model.PostalCode;
import fr.openwide.core.basicapp.core.business.referencedata.model.City;
import fr.openwide.core.jpa.business.generic.dao.IGenericEntityDao;

public interface ICityDao extends IGenericEntityDao<Long, City>{
	
	City getByLabelAndPostalCode(String label, PostalCode postalCode);

}
