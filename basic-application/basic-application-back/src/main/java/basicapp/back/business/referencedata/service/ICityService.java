package basicapp.back.business.referencedata.service;

import basicapp.back.business.common.model.PostalCode;
import basicapp.back.business.referencedata.model.City;
import org.iglooproject.jpa.business.generic.service.IGenericEntityService;
import org.jspecify.annotations.Nullable;

public interface ICityService extends IGenericEntityService<Long, City> {

  @Nullable City getByLabelAndPostalCode(@Nullable String label, @Nullable PostalCode postalCode);
}
