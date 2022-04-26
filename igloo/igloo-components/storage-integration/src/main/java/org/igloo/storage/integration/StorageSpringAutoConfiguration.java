package org.igloo.storage.integration;

import static org.igloo.storage.integration.StoragePropertyIds.DB_FICHIER_SEQUENCE_NAME;
import static org.igloo.storage.integration.StoragePropertyIds.DB_STORAGE_UNIT_SEQUENCE_NAME;
import static org.igloo.storage.integration.StoragePropertyIds.PATH;
import static org.igloo.storage.integration.StoragePropertyIds.STORAGE_UNIT_TYPE_CANDIDATES;
import static org.igloo.storage.integration.StoragePropertyIds.TRANSACTION_SYNCHRONIZATION_ORDER;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.EntityManagerFactory;

import org.igloo.storage.api.IMimeTypeResolver;
import org.igloo.storage.api.IStorageService;
import org.igloo.storage.impl.DatabaseOperations;
import org.igloo.storage.impl.MimeTypeResolver;
import org.igloo.storage.impl.StorageOperations;
import org.igloo.storage.impl.StorageService;
import org.igloo.storage.model.Fichier;
import org.igloo.storage.model.atomic.IStorageUnitType;
import org.iglooproject.jpa.config.spring.provider.JpaPackageScanProvider;
import org.iglooproject.spring.config.spring.IPropertyRegistryConfig;
import org.iglooproject.spring.property.service.IPropertyRegistry;
import org.iglooproject.spring.property.service.IPropertyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

@Configuration
@ConditionalOnProperty(name = "igloo-ac.storage.disabled", havingValue = "false", matchIfMissing = true)
public class StorageSpringAutoConfiguration implements IPropertyRegistryConfig {

	private static final Logger LOGGER = LoggerFactory.getLogger(StorageSpringAutoConfiguration.class);

	@Bean
	@ConditionalOnMissingBean
	public IStorageService storageService(EntityManagerFactory entityManagerFactory, IPropertyService propertyService,
			IMimeTypeResolver mimetypeResolver) {
		return new StorageService(
				propertyService.get(TRANSACTION_SYNCHRONIZATION_ORDER),
				new DatabaseOperations(
						entityManagerFactory,
						propertyService.get(DB_FICHIER_SEQUENCE_NAME),
						propertyService.get(DB_STORAGE_UNIT_SEQUENCE_NAME)),
				propertyService.get(STORAGE_UNIT_TYPE_CANDIDATES),
				new StorageOperations(),
				() -> Path.of(propertyService.get(StoragePropertyIds.PATH)),
				mimetypeResolver);
	}

	@Bean
	@ConditionalOnMissingBean
	public IMimeTypeResolver mimeTypeResolver() {
		return new MimeTypeResolver();
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
		// retrieve enum values from a ',' separated list. Each item can be an enum type OR a enum value
		// enum value must be written package.enumType.ENUM_VALUE
		registry.register(StoragePropertyIds.STORAGE_UNIT_TYPE_CANDIDATES, s -> {
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
	}

	private Set<IStorageUnitType> resolveIStorageUnitType(String className) throws ClassNotFoundException {
		Class<?> clazz = Class.forName(className);
		if (IStorageUnitType.class.isAssignableFrom(clazz) && Enum.class.isAssignableFrom(clazz)) {
			return Set.of(clazz.getEnumConstants()).stream().map(IStorageUnitType.class::cast).collect(Collectors.toUnmodifiableSet());
		} else {
			throw new ClassNotFoundException(String.format("Values from %s are not Enum&IStorageUnitType", className));
		}
	}

}
