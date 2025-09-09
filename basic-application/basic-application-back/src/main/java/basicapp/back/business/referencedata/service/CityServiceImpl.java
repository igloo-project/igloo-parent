package basicapp.back.business.referencedata.service;

import basicapp.back.business.common.model.PostalCode;
import basicapp.back.business.referencedata.model.City;
import basicapp.back.business.referencedata.repository.ICityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CityServiceImpl implements ICityService {

  private final ICityRepository cityRepository;

  @Autowired
  public CityServiceImpl(ICityRepository cityRepository) {
    this.cityRepository = cityRepository;
  }

  @Override
  @Transactional(readOnly = true)
  public City getByLabelAndPostalCode(String label, PostalCode postalCode) {
    return cityRepository.findByLabelFrAndPostalCode(label, postalCode);
  }
}
