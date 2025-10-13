package basicapp.back.business.referencedata.controller;

import static basicapp.back.security.model.BasicApplicationSecurityExpressionConstants.REFERENCE_DATA_WRITE;

import basicapp.back.business.common.model.PostalCode;
import basicapp.back.business.referencedata.model.City;
import org.iglooproject.commons.util.security.PermissionObject;
import org.springframework.security.access.prepost.PreAuthorize;

public interface ICityControllerService {

  @PreAuthorize(REFERENCE_DATA_WRITE)
  void save(@PermissionObject City city);

  City getByLabelAndPostalCode(String label, PostalCode postalCode);
}
