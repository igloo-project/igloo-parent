package org.igloo.storage.impl;

import java.util.Set;

import org.hibernate.boot.model.TypeContributions;
import org.hibernate.boot.model.TypeContributor;
import org.hibernate.service.ServiceRegistry;
import org.igloo.hibernate.type.InterfaceEnumMapperType;
import org.igloo.storage.model.atomic.IFichierType;
import org.igloo.storage.model.atomic.IStorageUnitType;
import org.igloo.storage.model.hibernate.StorageHibernateConstants;

public abstract class AbstractStorageTypeContributor implements TypeContributor {

	@Override
	public final void contribute(TypeContributions typeContributions, ServiceRegistry serviceRegistry) {
		typeContributions.contributeType(new InterfaceEnumMapperType<>(
				StorageHibernateConstants.TYPE_FICHIER_TYPE,
				IFichierType.class,
				getFichierTypeTypes()));
		typeContributions.contributeType(new InterfaceEnumMapperType<>(
				StorageHibernateConstants.TYPE_STORAGE_UNIT_TYPE,
				IStorageUnitType.class,
				getStorageUnitTypeTypes()));
	}

	public abstract Set<Class<? extends IFichierType>> getFichierTypeTypes();

	public abstract Set<Class<? extends IStorageUnitType>> getStorageUnitTypeTypes();

}
