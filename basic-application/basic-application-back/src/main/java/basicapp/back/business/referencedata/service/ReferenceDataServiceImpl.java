package basicapp.back.business.referencedata.service;

import basicapp.back.business.referencedata.model.ReferenceData;
import basicapp.back.business.referencedata.model.comparator.ReferenceDataComparator;
import basicapp.back.business.referencedata.predicate.ReferenceDataPredicates;
import basicapp.back.business.referencedata.repository.IReferenceDataRepository;
import com.google.common.base.Preconditions;
import java.util.List;
import java.util.Objects;
import org.springframework.context.ApplicationContext;
import org.springframework.data.repository.support.Repositories;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReferenceDataServiceImpl<T extends ReferenceData<?>>
    implements IReferenceDataService<T> {

  private final Repositories repositories;

  public ReferenceDataServiceImpl(ApplicationContext context) {
    this.repositories = new Repositories(context);
  }

  @Override
  @Transactional
  public void saveReferenceData(T referenceData, Class<T> clazz) {
    Objects.requireNonNull(referenceData);
    Objects.requireNonNull(clazz);
    Preconditions.checkArgument(ReferenceDataPredicates.editable().apply(referenceData));
    getRepository(clazz).save(referenceData);
  }

  @Override
  @Transactional(readOnly = true)
  public List<T> findAllEnabled(Class<T> clazz) {
    return clazz == null
        ? List.of()
        : getRepository(clazz).findAllByEnabledIsTrue().stream()
            .sorted(ReferenceDataComparator.get())
            .toList();
  }

  private IReferenceDataRepository<T> getRepository(Class<T> clazz) {
    return repositories
        .getRepositoryFor(clazz)
        .map(r -> (IReferenceDataRepository<T>) r)
        .orElseThrow();
  }
}
