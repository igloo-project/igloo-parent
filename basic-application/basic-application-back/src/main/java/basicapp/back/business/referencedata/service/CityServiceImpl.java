package basicapp.back.business.referencedata.service;

import basicapp.back.business.common.model.PostalCode;
import basicapp.back.business.referencedata.dao.ICityDao;
import basicapp.back.business.referencedata.model.City;
import org.iglooproject.jpa.business.generic.service.GenericEntityServiceImpl;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;

@Service
public class CityServiceImpl extends GenericEntityServiceImpl<Long, City> implements ICityService {

  private final ICityDao dao;

  public CityServiceImpl(ICityDao dao) {
    super(dao);
    this.dao = dao;
  }

  @Override
  public @Nullable City getByLabelAndPostalCode(
      @Nullable String label, @Nullable PostalCode postalCode) {
    if (label == null || postalCode == null) {
      return null;
    }
    return dao.getByLabelAndPostalCode(label, postalCode).orElse(null);
  }
}
