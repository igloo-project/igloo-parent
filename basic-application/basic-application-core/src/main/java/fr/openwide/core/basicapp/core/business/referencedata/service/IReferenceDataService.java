package fr.openwide.core.basicapp.core.business.referencedata.service;

import fr.openwide.core.basicapp.core.business.common.model.PostalCode;
import fr.openwide.core.basicapp.core.business.referencedata.model.City;

public interface IReferenceDataService {
	
	City getCityByLabelAndPostalCode(String label, PostalCode postalCode);

}
