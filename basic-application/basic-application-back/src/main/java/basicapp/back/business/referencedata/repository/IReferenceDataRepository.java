package basicapp.back.business.referencedata.repository;

import basicapp.back.business.referencedata.model.ReferenceData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface IReferenceDataRepository<T extends ReferenceData<?>>
    extends JpaRepository<T, Long>, JpaSpecificationExecutor<T> {}
