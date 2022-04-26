package org.igloo.storage.integration;

import static org.igloo.storage.integration.StoragePropertyIds.DB_FICHIER_SEQUENCE_NAME;
import static org.igloo.storage.integration.StoragePropertyIds.DB_STORAGE_UNIT_SEQUENCE_NAME;
import static org.igloo.storage.integration.StoragePropertyIds.PATH;
import static org.igloo.storage.integration.StoragePropertyIds.TRANSACTION_SYNCHRONIZATION_ORDER;

import java.nio.file.Path;
import java.util.Set;

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
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

@Configuration
@ConditionalOnProperty(name = "igloo-ac.storage.disabled", havingValue = "false", matchIfMissing = true)
public class StorageSpringAutoConfiguration implements IPropertyRegistryConfig {

	@Bean
	@ConditionalOnMissingBean
	public IStorageService storageService(EntityManagerFactory entityManagerFactory, IPropertyService propertyService,
			Set<IStorageUnitType> storageUnitTypes, IMimeTypeResolver mimetypeResolver) {
		return new StorageService(
				propertyService.get(StoragePropertyIds.TRANSACTION_SYNCHRONIZATION_ORDER),
				new DatabaseOperations(
						entityManagerFactory,
						propertyService.get(StoragePropertyIds.DB_FICHIER_SEQUENCE_NAME),
						propertyService.get(StoragePropertyIds.DB_STORAGE_UNIT_SEQUENCE_NAME)),
				storageUnitTypes,
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
	}

}
