package test.model;

import org.igloo.storage.model.atomic.IFichierType;

public enum FichierType implements IFichierType {
  TYPE1("type1"),
  TYPE2("type2");

  private final String path;

  FichierType(String path) {
    this.path = path;
  }

  @Override
  public String getDescription() {
    return getName();
  }

  @Override
  public String getPath() {
    return path;
  }

  @Override
  public String getName() {
    return name();
  }
}
