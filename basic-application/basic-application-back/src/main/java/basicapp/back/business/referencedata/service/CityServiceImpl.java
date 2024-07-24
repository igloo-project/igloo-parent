package basicapp.back.business.referencedata.service;

import basicapp.back.business.common.model.PostalCode;
import basicapp.back.business.referencedata.dao.ICityDao;
import basicapp.back.business.referencedata.model.City;
import org.iglooproject.jpa.business.generic.service.GenericEntityServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CityServiceImpl extends GenericEntityServiceImpl<Long, City> implements ICityService {

  private ICityDao dao;

  @Autowired
  public CityServiceImpl(ICityDao dao) {
    super(dao);
    this.dao = dao;
  }

  @Override
  public City getByLabelAndPostalCode(String label, PostalCode postalCode) {
    return dao.getByLabelAndPostalCode(label, postalCode);
  }
}
