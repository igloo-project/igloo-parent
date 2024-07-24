package test.hibernate;

import java.util.Set;
import org.igloo.storage.impl.AbstractStorageTypeContributor;
import org.igloo.storage.model.atomic.IFichierType;
import org.igloo.storage.model.atomic.IStorageUnitType;
import test.model.FichierType1;
import test.model.FichierType2;
import test.model.StorageUnitType;

public class StorageTypeContributor extends AbstractStorageTypeContributor {

  @Override
  public Set<Class<? extends IFichierType>> getFichierTypeTypes() {
    return Set.<Class<? extends IFichierType>>of(FichierType1.class, FichierType2.class);
  }

  @Override
  public Set<Class<? extends IStorageUnitType>> getStorageUnitTypeTypes() {
    return Set.<Class<? extends IStorageUnitType>>of(StorageUnitType.class);
  }
}
