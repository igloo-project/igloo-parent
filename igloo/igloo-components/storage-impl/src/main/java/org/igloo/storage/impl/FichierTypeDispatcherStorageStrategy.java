package org.igloo.storage.impl;

import java.util.Optional;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.igloo.storage.model.IFichierType;
import org.igloo.storage.model.StorageUnit;
import org.springframework.orm.jpa.EntityManagerFactoryUtils;

/**
 * {@link IStorageStrategy} that dispatch storage on a set of {@link IStorageUnitType} based on 
 */
public class FichierTypeDispatcherStorageStrategy implements IStorageStrategy {

	private final Set<IStorageUnitType> storageUnitTypes;

	private final EntityManagerFactory entityManagerFactory;

	public FichierTypeDispatcherStorageStrategy(EntityManagerFactory entityManagerFactory, Set<IStorageUnitType> storageUnitTypes) {
		this.storageUnitTypes = storageUnitTypes;
		this.entityManagerFactory = entityManagerFactory;
	}

	@Override
	public StorageUnit select(IFichierType type) {
		// TODO custom Exception
		IStorageUnitType unitType = storageUnitTypes.stream().filter(s -> s.accept(type)).findFirst().orElseThrow();
		return entityManager()
			.createQuery("FROM StorageUnit s WHERE s.unitType = :type AND s.status = :status ORDER BY s.id DESC", StorageUnit.class)
			.setParameter("type", unitType)
			.getResultStream()
			.findFirst()
			.orElseThrow();
	}

	@Nonnull
	private EntityManager entityManager() {
		return Optional.ofNullable(EntityManagerFactoryUtils.getTransactionalEntityManager(entityManagerFactory)).orElseThrow();
	}

}
