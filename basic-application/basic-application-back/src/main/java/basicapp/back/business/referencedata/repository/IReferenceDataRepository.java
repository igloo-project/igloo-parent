package basicapp.back.business.referencedata.repository;

import basicapp.back.business.referencedata.model.ReferenceData;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface IReferenceDataRepository<T extends ReferenceData<?>>
    extends JpaRepository<T, Long>, JpaSpecificationExecutor<T> {
  List<T> findAllByEnabledIsTrue();
}
