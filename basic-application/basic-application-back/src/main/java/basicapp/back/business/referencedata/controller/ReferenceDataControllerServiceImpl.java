package basicapp.back.business.referencedata.controller;

import basicapp.back.business.referencedata.model.ReferenceData;
import basicapp.back.business.referencedata.service.IReferenceDataService;
import java.util.List;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.stereotype.Service;

@Service
public class ReferenceDataControllerServiceImpl<T extends ReferenceData<? super T>>
    implements IReferenceDataControllerService<T> {

  private final IReferenceDataService<T> referenceDataService;

  public ReferenceDataControllerServiceImpl(IReferenceDataService<T> referenceDataService) {
    this.referenceDataService = referenceDataService;
  }

  @Override
  public void saveReferenceData(T referenceData, Class<T> clazz) {
    referenceDataService.saveReferenceData(referenceData, clazz);
  }

  @Override
  public List<T> findAllEnabled(Class<T> clazz) {
    return referenceDataService.findAllEnabled(clazz);
  }
}
