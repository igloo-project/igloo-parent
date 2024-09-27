package test.core.metamodel;

import basicapp.back.business.common.model.PostalCode;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Import;
import test.core.AbstractBasicApplicationTestCase;
import test.core.PSQLTestContainerConfiguration;
import test.core.config.spring.SpringBootTestBasicApplication;

@SpringBootTestBasicApplication
@Import(PSQLTestContainerConfiguration.class)
class TestMetaModel extends AbstractBasicApplicationTestCase {

  @Test
  void testMetaModel() throws NoSuchFieldException, SecurityException {
    // Class<?> est utilisé sur GenericEntityReference ; ATTENTION,
    // l'annotation @Type est nécessaire pour un traitement correct par Hibernate.
    super.testMetaModel(PostalCode.class, Class.class, Comparable.class);
  }
}
