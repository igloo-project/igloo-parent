package basicapp.back.business.referencedata.repository;

import basicapp.back.business.common.model.PostalCode;
import basicapp.back.business.referencedata.model.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ICityRepository extends JpaRepository<City, Long>, JpaSpecificationExecutor<City> {
  City findByLabelFrAndPostalCode(String labelFr, PostalCode postalCode);
}
