package basicapp.back.business.referencedata.service;

import basicapp.back.business.common.model.PostalCode;
import basicapp.back.business.referencedata.model.City;

public interface ICityService {

  void save(City city);

  City getByLabelAndPostalCode(String label, PostalCode postalCode);
}
