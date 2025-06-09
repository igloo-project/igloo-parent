package test.model;

import java.util.Set;
import org.igloo.storage.impl.FichierPathStrategy;
import org.igloo.storage.model.atomic.IFichierPathStrategy;
import org.igloo.storage.model.atomic.IFichierType;
import org.igloo.storage.model.atomic.IStorageUnitType;

public enum StorageUnitType implements IStorageUnitType {
  SOURCE("source", Set.of(FichierType.TYPE1, FichierType.TYPE2), new FichierPathStrategy(1)),
  TARGET("target", Set.of(), new FichierPathStrategy(1));

  private final String path;
  private final Set<IFichierType> acceptedFichierTypes;
  private final IFichierPathStrategy fichierPathStrategy;

  StorageUnitType(
      String path,
      Set<IFichierType> acceptedFichierTypes,
      IFichierPathStrategy fichierPathStrategy) {
    this.path = path;
    this.acceptedFichierTypes = acceptedFichierTypes;
    this.fichierPathStrategy = fichierPathStrategy;
  }

  @Override
  public String getDescription() {
    return name();
  }

  @Override
  public String getPath() {
    return path;
  }

  @Override
  public boolean accept(IFichierType type) {
    return acceptedFichierTypes.contains(type);
  }

  @Override
  public Set<IFichierType> getAcceptedFichierTypes() {
    return acceptedFichierTypes;
  }

  @Override
  public IFichierPathStrategy getFichierPathStrategy() {
    return fichierPathStrategy;
  }

  @Override
  public String getName() {
    return name();
  }
}
