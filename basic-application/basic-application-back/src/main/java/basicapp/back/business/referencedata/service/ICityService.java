package basicapp.back.business.referencedata.service;

import basicapp.back.business.common.model.PostalCode;
import basicapp.back.business.referencedata.model.City;
import org.iglooproject.jpa.business.generic.service.IGenericEntityService;

public interface ICityService extends IGenericEntityService<Long, City> {

  City getByLabelAndPostalCode(String label, PostalCode postalCode);
}
