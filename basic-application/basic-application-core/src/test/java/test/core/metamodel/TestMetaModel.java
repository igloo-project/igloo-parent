package test.core.metamodel;

import org.iglooproject.basicapp.core.business.common.model.PostalCode;
import org.iglooproject.config.bootstrap.spring.ExtendedApplicationContextInitializer;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

import test.core.AbstractBasicApplicationTestCase;
import test.core.config.spring.BasicApplicationCoreTestCommonConfig;

@SpringBootTest(classes = BasicApplicationCoreTestCommonConfig.class)
@ContextConfiguration(initializers = ExtendedApplicationContextInitializer.class)
@TestPropertySource(properties = "igloo.profile=test")
class TestMetaModel extends AbstractBasicApplicationTestCase {

	@Test
	void testMetaModel() throws NoSuchFieldException, SecurityException {
		// Class<?> est utilisé sur GenericEntityReference ; ATTENTION,
		// l'annotation @Type est nécessaire pour un traitement correct par Hibernate.
		super.testMetaModel(PostalCode.class, Class.class, Comparable.class);
	}

}
