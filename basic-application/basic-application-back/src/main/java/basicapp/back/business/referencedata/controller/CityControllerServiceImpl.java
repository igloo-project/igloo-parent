package basicapp.back.business.referencedata.controller;

import basicapp.back.business.common.model.PostalCode;
import basicapp.back.business.referencedata.model.City;
import basicapp.back.business.referencedata.service.ICityService;
import org.springframework.stereotype.Service;

@Service
public class CityControllerServiceImpl implements ICityControllerService {

  private final ICityService cityService;

  public CityControllerServiceImpl(ICityService cityService) {
    this.cityService = cityService;
  }

  @Override
  public void save(City city) {
    cityService.save(city);
  }

  @Override
  public City getByLabelAndPostalCode(String label, PostalCode postalCode) {
    return cityService.getByLabelAndPostalCode(label, postalCode);
  }
}
