package org.iglooproject.basicapp.core.business.referencedata.service;

import org.iglooproject.basicapp.core.business.common.model.PostalCode;
import org.iglooproject.basicapp.core.business.referencedata.model.City;
import org.iglooproject.jpa.business.generic.service.IGenericEntityService;

public interface ICityService extends IGenericEntityService<Long, City> {

	City getByLabelAndPostalCode(String label, PostalCode postalCode);

}
