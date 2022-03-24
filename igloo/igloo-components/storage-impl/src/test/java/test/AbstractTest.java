package test;

import static org.mockito.Mockito.mock;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.persistence.EntityManagerFactory;

import org.hibernate.cfg.AvailableSettings;
import org.hibernate.dialect.PostgreSQL10Dialect;
import org.igloo.jpa.test.EntityManagerFactoryExtension;
import org.igloo.storage.api.IStorageService;
import org.igloo.storage.impl.SequenceGenerator;
import org.igloo.storage.impl.StorageOperations;
import org.igloo.storage.impl.StorageService;
import org.igloo.storage.model.Fichier;
import org.igloo.storage.model.StorageFailure;
import org.igloo.storage.model.StorageUnit;
import org.igloo.storage.model.StorageUnitStatistics;
import org.igloo.storage.model.atomic.IFichierType;
import org.igloo.storage.model.atomic.IStorageUnitType;
import org.iglooproject.jpa.business.generic.model.GenericEntity;
import org.iglooproject.jpa.hibernate.model.naming.ImplicitNamingStrategyJpaComponentPathImpl;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.postgresql.Driver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.interceptor.DefaultTransactionAttribute;
import org.springframework.transaction.support.TransactionTemplate;

import test.model.StorageUnitType;

class AbstractTest {
	private static final List<Class<? extends GenericEntity<Long, ?>>> ENTITIES = List.of(Fichier.class, StorageUnit.class, StorageUnitStatistics.class, StorageFailure.class);
	private static final String CFG_DB_TYPE = "TEST_DB_TYPE";
	private static final String CFG_DB_NAME = "TEST_DB_NAME";
	private static final String CFG_DB_HOST = "TEST_DB_HOST";
	private static final String CFG_DB_PORT = "TEST_DB_PORT";
	private static final String CFG_DB_SCHEMA = "TEST_DB_SCHEMA";
	private static final String CFG_TYPE_POSTGRESQL = "postgresql";
	private static final String CFG_TYPE_H2 = "h2";
	private static final String DEFAULT_USER = "igloo_test";
	private static final String DEFAULT_PASSWORD = "TEST_DB_PASSWORD";
	private static final String DEFAULT_NAME = DEFAULT_USER;
	private static final String DEFAULT_HOST = "localhost";
	private static final String DEFAULT_PORT = "5436";
	protected static final Logger LOGGER = LoggerFactory.getLogger(AbstractTest.class);
	protected static final String FILE_CONTENT = "blabla";
	protected static final String FILE_CHECKSUM_SHA_256 = "ccadd99b16cd3d200c22d6db45d8b6630ef3d936767127347ec8a76ab992c2ea";
	protected static final long FILE_SIZE = 6L;
	static {
		// jboss-logging -> slf4j
		System.setProperty("org.jboss.logging.provider", "slf4j");
	}
	@RegisterExtension
	EntityManagerFactoryExtension extension = new EntityManagerFactoryExtension(() -> {
		List<Object> settings = new ArrayList<>();
		settings.addAll(List.of(
				AvailableSettings.HBM2DDL_AUTO, "create",
				AvailableSettings.LOADED_CLASSES, ENTITIES,
				AvailableSettings.XML_MAPPING_ENABLED, Boolean.FALSE.toString(),
				AvailableSettings.IMPLICIT_NAMING_STRATEGY, ImplicitNamingStrategyJpaComponentPathImpl.INSTANCE,
				AvailableSettings.HBM2DDL_CREATE_SOURCE, "metadata-then-script",
				AvailableSettings.HBM2DDL_DROP_SOURCE, "script-then-metadata",
				AvailableSettings.HBM2DDL_CREATE_SCRIPT_SOURCE, new StringReader("create sequence fichier_id_seq; create sequence storageunit_id_seq;"),
				AvailableSettings.HBM2DDL_DROP_SCRIPT_SOURCE, new StringReader("drop sequence if exists fichier_id_seq; drop sequence if exists storageunit_id_seq")
		));
		String type = Optional.ofNullable(System.getenv(CFG_DB_TYPE)).orElse(CFG_TYPE_H2);
		if (CFG_TYPE_H2.equals(type)) {
			settings.addAll(List.of(
				AvailableSettings.DIALECT, org.hibernate.dialect.H2Dialect.class.getName(),
				AvailableSettings.JPA_JDBC_DRIVER, org.h2.Driver.class.getName(),
				AvailableSettings.JPA_JDBC_URL, "jdbc:h2:mem:storage;INIT=create schema if not exists storage"));
		} else if (CFG_TYPE_POSTGRESQL.equals(type)) {
			String port = Optional.ofNullable(System.getenv(CFG_DB_PORT)).orElse(DEFAULT_PORT);
			String host = Optional.ofNullable(System.getenv(CFG_DB_HOST)).orElse(DEFAULT_HOST);
			String name = Optional.ofNullable(System.getenv(CFG_DB_NAME)).orElse(DEFAULT_NAME);
			settings.addAll(List.of(
					AvailableSettings.DIALECT, PostgreSQL10Dialect.class.getName(),
					AvailableSettings.JPA_JDBC_DRIVER, Driver.class.getName(),
					AvailableSettings.JPA_JDBC_USER, Optional.ofNullable(System.getenv(CFG_DB_NAME)).orElse(DEFAULT_USER),
					AvailableSettings.DEFAULT_SCHEMA, Optional.ofNullable(System.getenv(CFG_DB_SCHEMA)).orElse(DEFAULT_USER),
					AvailableSettings.JPA_JDBC_PASSWORD, Optional.ofNullable(System.getenv(DEFAULT_PASSWORD)).orElse(DEFAULT_NAME),
					AvailableSettings.JPA_JDBC_URL, String.format("jdbc:postgresql://%s:%s/%s", host, port, name)));
		} else {
			throw new IllegalStateException(String.format("Unknown value %s for TEST_DB_TYPE", type));
		}
		
		return settings.toArray();
	});
	protected IStorageService storageService;
	protected TransactionTemplate transactionTemplate;
	protected final StorageOperations operations = mock(StorageOperations.class);

	void initStorageUnit(EntityManagerFactory entityManagerFactory, Path tempDir) {
		this.storageService = new StorageService(entityManagerFactory, Ordered.LOWEST_PRECEDENCE, new SequenceGenerator("fichier_id_seq"), Set.<IStorageUnitType>copyOf(EnumSet.allOf(StorageUnitType.class)), operations, () -> tempDir);
		PlatformTransactionManager transactionManager = new JpaTransactionManager(entityManagerFactory);
		transactionTemplate = new TransactionTemplate(transactionManager, writeTransactionAttribute());
		transactionTemplate.executeWithoutResult((t) -> storageService.createStorageUnit(StorageUnitType.TYPE_1));
	}

	protected Fichier createFichier(String filename, IFichierType fichierType, String fileContent, Runnable postCreationAction) {
		return transactionTemplate.execute((t) -> {
			try (InputStream bais = new ByteArrayInputStream(fileContent.getBytes(StandardCharsets.UTF_8))) {
				return storageService.addFichier(filename, fichierType, bais);
			} catch (IOException e) {
				throw new IllegalStateException(e);
			} finally {
				postCreationAction.run();
			}
		});
	}

	protected DefaultTransactionAttribute writeTransactionAttribute() {
		DefaultTransactionAttribute ta = new DefaultTransactionAttribute();
		ta.setReadOnly(false);
		return ta;
	}
}
