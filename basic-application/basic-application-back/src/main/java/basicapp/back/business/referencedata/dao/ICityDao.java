package basicapp.back.business.referencedata.dao;

import org.iglooproject.jpa.business.generic.dao.IGenericEntityDao;

import basicapp.back.business.common.model.PostalCode;
import basicapp.back.business.referencedata.model.City;

public interface ICityDao extends IGenericEntityDao<Long, City>{

	City getByLabelAndPostalCode(String label, PostalCode postalCode);

}
