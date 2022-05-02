package org.igloo.storage.integration;

import static org.igloo.storage.integration.StoragePropertyIds.*;

import java.nio.file.Path;
import java.time.Duration;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.persistence.EntityManagerFactory;

import org.igloo.storage.api.IMimeTypeResolver;
import org.igloo.storage.api.IStorageHousekeepingService;
import org.igloo.storage.api.IStorageService;
import org.igloo.storage.impl.DatabaseOperations;
import org.igloo.storage.impl.MimeTypeResolver;
import org.igloo.storage.impl.StorageHousekeepingService;
import org.igloo.storage.impl.StorageOperations;
import org.igloo.storage.impl.StorageService;
import org.igloo.storage.model.Fichier;
import org.igloo.storage.model.atomic.IStorageUnitType;
import org.iglooproject.jpa.config.spring.provider.JpaPackageScanProvider;
import org.iglooproject.spring.config.spring.IPropertyRegistryConfig;
import org.iglooproject.spring.property.model.ImmutablePropertyId;
import org.iglooproject.spring.property.service.IPropertyRegistry;
import org.iglooproject.spring.property.service.IPropertyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.scheduling.annotation.SchedulingConfiguration;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.transaction.PlatformTransactionManager;

import com.google.common.base.Strings;

@Configuration
@ConditionalOnProperty(name = "igloo-ac.storage.disabled", havingValue = "false", matchIfMissing = true)
public class StorageSpringAutoConfiguration implements IPropertyRegistryConfig {

	private static final Logger LOGGER = LoggerFactory.getLogger(StorageSpringAutoConfiguration.class);

	@Bean
	public DatabaseOperations databaseOperations(EntityManagerFactory entityManagerFactory, IPropertyService propertyService) {
		return new DatabaseOperations(
				entityManagerFactory,
				propertyService.get(DB_FICHIER_SEQUENCE_NAME),
				propertyService.get(DB_STORAGE_UNIT_SEQUENCE_NAME));
	}

	@Bean
	@ConditionalOnMissingBean
	public IStorageService storageService(IPropertyService propertyService, DatabaseOperations databaseOperations,
			IMimeTypeResolver mimetypeResolver) {
		return new StorageService(
				propertyService.get(TRANSACTION_SYNCHRONIZATION_ORDER),
				databaseOperations,
				propertyService.get(STORAGE_UNIT_TYPE_CANDIDATES),
				new StorageOperations(),
				() -> Path.of(propertyService.get(PATH)),
				mimetypeResolver);
	}

	@Bean
	@ConditionalOnMissingBean
	public IMimeTypeResolver mimeTypeResolver() {
		return new MimeTypeResolver();
	}

	@Bean
	@ConditionalOnMissingBean
	public IStorageHousekeepingService storageHousekeepingService(PlatformTransactionManager platformTransactionManager,
			DatabaseOperations databaseOperations, IStorageService storageService, IPropertyService propertyService) {
		return new StorageHousekeepingService(platformTransactionManager,
				storageService,
				databaseOperations,
				propertyService.get(JOB_CHECK_DEFAULT_DELAY),
				propertyService.get(JOB_CHECK_CHECKSUM_DEFAULT_DELAY),
				propertyService.get(JOB_CLEAN_TRANSIENT_DELAY),
				propertyService.get(JOB_CLEAN_LIMIT),
				propertyService.get(JOB_CONSISTENCY_STORAGE_UNIT_LIMIT) <= 0 ? null : propertyService.get(JOB_CONSISTENCY_STORAGE_UNIT_LIMIT));
	}

	@Bean
	public JpaPackageScanProvider storagePackageScanProvider() {
		return new JpaPackageScanProvider(Fichier.class.getPackage());
	}

	@Override
	public void register(IPropertyRegistry registry) {
		registry.registerInteger(TRANSACTION_SYNCHRONIZATION_ORDER, Ordered.LOWEST_PRECEDENCE);
		registry.registerString(PATH);
		registry.registerString(DB_FICHIER_SEQUENCE_NAME, "fichier_id_seq");
		registry.registerString(DB_STORAGE_UNIT_SEQUENCE_NAME, "storageUnit_id_seq");
		registry.registerBoolean(JOB_DISABLED, false);
		registry.register(JOB_CLEANING_CRON, s -> cronTrigger(s, "0 0 22 * * ?"));
		registry.registerBoolean(JOB_CLEANING_INVALIDATED_DISABLED, false);
		registry.registerBoolean(JOB_CLEANING_TRANSIENT_DISABLED, false);
		registry.registerBoolean(JOB_CONSISTENCY_DISABLED, false);
		registry.registerBoolean(JOB_SPLIT_STORAGE_UNIT_DISABLED, false);
		registry.register(JOB_HOUSEKEEPING_CRON, s -> cronTrigger(s, "0 0 23 * * ?"));
		// retrieve enum values from a ',' separated list. Each item can be an enum type OR a enum value
		// enum value must be written package.enumType.ENUM_VALUE
		registry.register(STORAGE_UNIT_TYPE_CANDIDATES, s -> {
			Set<IStorageUnitType> types = new HashSet<>();
			Set<String> values = Arrays.stream(s.split(",")).collect(Collectors.toUnmodifiableSet());
			for (String value : values) {
				value = value.strip();
				try {
					types.addAll(resolveIStorageUnitType(value));
				} catch (ClassNotFoundException e) {
					LOGGER.debug("{} cannot be resolved to a class. Resolving as an enum value.", value);
					if (value.contains(".")) {
						try {
							String className = value.substring(0, value.lastIndexOf("."));
							String enumName = value.substring(value.lastIndexOf(".") + 1);
							resolveIStorageUnitType(className).stream().filter(i -> enumName.equals(i.getName()))
								.findFirst().ifPresentOrElse(types::add, () -> { throw new IllegalStateException(); });
						} catch (ClassNotFoundException|IllegalStateException e1) {
							throw new IllegalStateException(String.format("%s cannot be resolved to a value. Check value.", value));
						}
					}
				}
			}
			return types;
		});
		registry.register(JOB_CHECK_DEFAULT_DELAY, s -> StorageSpringAutoConfiguration.extractDuration(s, Duration.ofDays(3)));
		registry.register(JOB_CHECK_CHECKSUM_DEFAULT_DELAY, s -> StorageSpringAutoConfiguration.extractDuration(s, Duration.ofDays(15)));
		registry.register(JOB_CLEAN_TRANSIENT_DELAY, s -> StorageSpringAutoConfiguration.extractDuration(s, Duration.ofHours(5)));
		registry.registerInteger(JOB_CLEAN_LIMIT, 500);
		registry.registerInteger(JOB_CONSISTENCY_STORAGE_UNIT_LIMIT, 0);
	}

	private static CronTrigger cronTrigger(String value, String defaultCron) {
		return Optional.ofNullable(value).filter(Predicate.not(Strings::isNullOrEmpty)).map(CronTrigger::new).orElseGet(() -> new CronTrigger(defaultCron));
	}

	private static Duration extractDuration(String value, Duration defaultValue) {
		return Optional.ofNullable(value).filter(Predicate.not(Strings::isNullOrEmpty)).map(Duration::parse).orElse(defaultValue);
	}

	private Set<IStorageUnitType> resolveIStorageUnitType(String className) throws ClassNotFoundException {
		Class<?> clazz = Class.forName(className);
		if (IStorageUnitType.class.isAssignableFrom(clazz) && Enum.class.isAssignableFrom(clazz)) {
			return Set.of(clazz.getEnumConstants()).stream().map(IStorageUnitType.class::cast).collect(Collectors.toUnmodifiableSet());
		} else {
			throw new ClassNotFoundException(String.format("Values from %s are not Enum&IStorageUnitType", className));
		}
	}

	@Configuration
	@ConditionalOnMissingBean(IStorageScheduling.class)
	@ConditionalOnBean(SchedulingConfiguration.class)
	public static class DefaultStorageScheduling {
		@Bean
		public ScheduledTaskRegistrar storageTaskRegistrar(IStorageHousekeepingService housekeepingService, IPropertyService propertyService) {
			ScheduledTaskRegistrar scheduledTaskRegistrar = new ScheduledTaskRegistrar();
			if (Boolean.TRUE.equals(propertyService.get(JOB_DISABLED))) {
				LOGGER.warn("Storage jobs disabled by configuration (check {})", JOB_DISABLED.getKey());
			} else {
				if (propertyService.get(JOB_CLEANING_CRON) != null) {
					registerTask(
							propertyService,
							scheduledTaskRegistrar, JOB_CLEANING_CRON,
							() -> housekeepingService.cleaning(propertyService.get(JOB_CLEANING_INVALIDATED_DISABLED), propertyService.get(JOB_CLEANING_TRANSIENT_DISABLED)));
				} else {
					LOGGER.warn("Storage cleaning job disabled by explicit configuration (check {})", JOB_CLEANING_CRON.getKey());
				}
				
				if (propertyService.get(JOB_HOUSEKEEPING_CRON) != null) {
					registerTask(propertyService, scheduledTaskRegistrar, JOB_HOUSEKEEPING_CRON,
							() -> housekeepingService.housekeeping(propertyService.get(JOB_CONSISTENCY_DISABLED), propertyService.get(JOB_SPLIT_STORAGE_UNIT_DISABLED)));
				} else {
					LOGGER.warn("Storage housekeeping job disabled by explicit configuration (check {})", JOB_HOUSEKEEPING_CRON.getKey());
				}
			}
			return scheduledTaskRegistrar;
		}

		private void registerTask(IPropertyService propertyService, ScheduledTaskRegistrar registrar, ImmutablePropertyId<CronTrigger> cronPropertyId,
				Runnable task) {
			registrar.addCronTask(task, propertyService.getAsString(cronPropertyId));
		}
	}

}