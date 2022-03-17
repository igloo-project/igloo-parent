package test.hibernate;

import java.util.Set;

import org.hibernate.boot.model.TypeContributions;
import org.hibernate.boot.model.TypeContributor;
import org.hibernate.service.ServiceRegistry;
import org.igloo.hibernate.type.InterfaceEnumMapperType;
import org.igloo.storage.model.atomic.IFichierType;
import org.igloo.storage.model.atomic.IStorageUnitType;
import org.igloo.storage.model.hibernate.StorageHibernateConstants;

import test.model.FichierType1;
import test.model.FichierType2;
import test.model.StorageUnitType;

public class StorageTypeContributor implements TypeContributor {

	@Override
	public void contribute(TypeContributions typeContributions, ServiceRegistry serviceRegistry) {
		typeContributions.contributeType(new InterfaceEnumMapperType<>(
				StorageHibernateConstants.TYPE_FICHIER_TYPE,
				IFichierType.class,
				Set.<Class<? extends IFichierType>>of(FichierType1.class, FichierType2.class)));
		typeContributions.contributeType(new InterfaceEnumMapperType<>(
				StorageHibernateConstants.TYPE_STORAGE_UNIT_TYPE,
				IStorageUnitType.class,
				Set.<Class<? extends IStorageUnitType>>of(StorageUnitType.class)));
	}

}
