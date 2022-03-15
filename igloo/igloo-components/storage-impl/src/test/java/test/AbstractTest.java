package test;

import java.util.Arrays;

import org.hibernate.cfg.AvailableSettings;
import org.igloo.jpa.test.EntityManagerFactoryExtension;
import org.igloo.storage.model.Fichier;
import org.igloo.storage.model.StorageUnit;
import org.igloo.storage.model.StorageUnitStatistics;
import org.iglooproject.jpa.hibernate.model.naming.ImplicitNamingStrategyJpaComponentPathImpl;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class AbstractTest {
	protected static final Logger LOGGER = LoggerFactory.getLogger(AbstractTest.class);
	static {
		// jboss-logging -> slf4j
		System.setProperty("org.jboss.logging.provider", "slf4j");
	}
	@RegisterExtension
	EntityManagerFactoryExtension extension = new EntityManagerFactoryExtension(
			AvailableSettings.DIALECT, org.hibernate.dialect.H2Dialect.class.getName(),
			AvailableSettings.HBM2DDL_AUTO, "create",
			AvailableSettings.JPA_JDBC_DRIVER, org.h2.Driver.class.getName(),
			AvailableSettings.JPA_JDBC_URL, "jdbc:h2:mem:storage;INIT=create schema if not exists storage",
			AvailableSettings.LOADED_CLASSES, Arrays.asList(Fichier.class, StorageUnit.class, StorageUnitStatistics.class),
			AvailableSettings.XML_MAPPING_ENABLED, Boolean.FALSE.toString(),
			AvailableSettings.IMPLICIT_NAMING_STRATEGY, ImplicitNamingStrategyJpaComponentPathImpl.INSTANCE);
}
