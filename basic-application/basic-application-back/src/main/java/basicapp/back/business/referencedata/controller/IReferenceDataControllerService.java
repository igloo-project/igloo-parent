package basicapp.back.business.referencedata.controller;

import static basicapp.back.security.model.BasicApplicationSecurityExpressionConstants.REFERENCE_DATA_WRITE;

import basicapp.back.business.referencedata.model.ReferenceData;
import java.util.List;
import org.springframework.security.access.prepost.PreAuthorize;

public interface IReferenceDataControllerService<T extends ReferenceData<? super T>> {

  @PreAuthorize(REFERENCE_DATA_WRITE)
  void saveReferenceData(T referenceData, Class<T> clazz);

  List<T> findAllEnabled(Class<T> clazz);
}
