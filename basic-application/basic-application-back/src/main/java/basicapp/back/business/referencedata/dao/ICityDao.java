package basicapp.back.business.referencedata.dao;

import basicapp.back.business.common.model.PostalCode;
import basicapp.back.business.referencedata.model.City;
import org.iglooproject.jpa.business.generic.dao.IGenericEntityDao;

public interface ICityDao extends IGenericEntityDao<Long, City> {

  City getByLabelAndPostalCode(String label, PostalCode postalCode);
}
