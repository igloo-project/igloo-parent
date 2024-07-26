package test.model;

import org.igloo.storage.model.atomic.IFichierType;

public enum FichierType1 implements IFichierType {
  CONTENT1("content1"),
  CONTENT2("content2");

  private final String path;

  private FichierType1(String path) {
    this.path = path;
  }

  @Override
  public String getName() {
    return name();
  }

  @Override
  public String getDescription() {
    return name();
  }

  @Override
  public String getPath() {
    return path;
  }
}
