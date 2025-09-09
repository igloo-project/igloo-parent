package basicapp.back.business.referencedata.service;

import basicapp.back.business.referencedata.model.ReferenceData;

public interface IReferenceDataService<T extends ReferenceData<?>> {
  void saveReferenceData(T referenceData);
}
