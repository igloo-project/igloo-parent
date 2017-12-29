package org.iglooproject.basicapp.core.business.referencedata.service;

import org.iglooproject.basicapp.core.business.common.model.PostalCode;
import org.iglooproject.basicapp.core.business.referencedata.model.City;

public interface IReferenceDataService {
	
	City getCityByLabelAndPostalCode(String label, PostalCode postalCode);

}
