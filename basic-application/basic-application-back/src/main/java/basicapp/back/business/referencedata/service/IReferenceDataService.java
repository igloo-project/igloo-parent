package basicapp.back.business.referencedata.service;

import basicapp.back.business.referencedata.model.ReferenceData;
import java.util.List;

public interface IReferenceDataService<T extends ReferenceData<?>> {
  void saveReferenceData(T referenceData, Class<T> clazz);

  List<T> findAllEnabled(Class<T> clazz);
}
