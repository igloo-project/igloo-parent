package basicapp.back.business.referencedata.repository;

import basicapp.back.business.common.model.PostalCode;
import basicapp.back.business.referencedata.model.City;

public interface ICityRepository extends IReferenceDataRepository<City> {
  City findByLabelFrAndPostalCode(String labelFr, PostalCode postalCode);
}
