package basicapp.back.business.referencedata.service;

import basicapp.back.business.referencedata.model.ReferenceData;
import basicapp.back.business.referencedata.predicate.ReferenceDataPredicates;
import basicapp.back.business.referencedata.repository.IReferenceDataRepository;
import com.google.common.base.Preconditions;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReferenceDataServiceImpl<T extends ReferenceData<?>>
    implements IReferenceDataService<T> {

  private final IReferenceDataRepository<T> referenceDataRepository;

  @Autowired
  public ReferenceDataServiceImpl(IReferenceDataRepository<T> referenceDataRepository) {
    this.referenceDataRepository = referenceDataRepository;
  }

  @Transactional
  @Override
  public void saveReferenceData(T referenceData) {
    Objects.requireNonNull(referenceData);
    Preconditions.checkArgument(ReferenceDataPredicates.editable().apply(referenceData));
    referenceDataRepository.save(referenceData);
  }
}
