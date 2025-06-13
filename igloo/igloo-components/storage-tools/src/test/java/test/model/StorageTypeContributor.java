package test.model;

import java.util.Set;
import org.igloo.storage.impl.AbstractStorageTypeContributor;
import org.igloo.storage.model.atomic.IFichierType;
import org.igloo.storage.model.atomic.IStorageUnitType;

public class StorageTypeContributor extends AbstractStorageTypeContributor {

  @Override
  public Set<Class<? extends IFichierType>> getFichierTypeTypes() {
    return Set.<Class<? extends IFichierType>>of(FichierType.class);
  }

  @Override
  public Set<Class<? extends IStorageUnitType>> getStorageUnitTypeTypes() {
    return Set.<Class<? extends IStorageUnitType>>of(StorageUnitType.class);
  }
}
