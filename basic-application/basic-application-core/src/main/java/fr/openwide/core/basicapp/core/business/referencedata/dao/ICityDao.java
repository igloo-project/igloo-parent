package fr.openwide.core.basicapp.core.business.referencedata.dao;

import fr.openwide.core.basicapp.core.business.common.model.PostalCode;
import fr.openwide.core.basicapp.core.business.referencedata.model.City;

public interface ICityDao {
	
	City getByLabelAndPostalCode(String label, PostalCode postalCode);

}
