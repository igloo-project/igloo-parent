package org.iglooproject.basicapp.core.business.referencedata.dao;

import org.iglooproject.basicapp.core.business.referencedata.model.City;
import org.iglooproject.jpa.business.generic.dao.IGenericEntityDao;

public interface ICityDao extends IGenericEntityDao<Long, City>{

	//TODO: igloo-boot
	City getByLabelAndPostalCode(String label/*, PostalCode postalCode*/);

}
