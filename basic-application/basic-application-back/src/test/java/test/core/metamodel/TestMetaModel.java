package test.core.metamodel;

import basicapp.back.business.common.model.EmailAddress;
import basicapp.back.business.common.model.PhoneNumber;
import basicapp.back.business.common.model.PostalCode;
import java.util.Locale;
import org.junit.jupiter.api.Test;
import test.core.AbstractBasicApplicationTestCase;
import test.core.config.spring.BasicApplicationBackSpringBootTest;

@BasicApplicationBackSpringBootTest
class TestMetaModel extends AbstractBasicApplicationTestCase {

  @Test
  void testMetaModel() throws NoSuchFieldException, SecurityException {
    // Class<?> est utilisé sur GenericEntityReference ; ATTENTION,
    // l'annotation @Type est nécessaire pour un traitement correct par Hibernate.
    super.testMetaModel(
        EmailAddress.class,
        PhoneNumber.class,
        PostalCode.class,
        Locale.class,
        Class.class,
        Comparable.class);
  }
}
