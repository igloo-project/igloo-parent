package basicapp.back.business.referencedata.service;

import org.iglooproject.jpa.business.generic.service.IGenericEntityService;

import basicapp.back.business.common.model.PostalCode;
import basicapp.back.business.referencedata.model.City;

public interface ICityService extends IGenericEntityService<Long, City> {

	City getByLabelAndPostalCode(String label, PostalCode postalCode);

}
