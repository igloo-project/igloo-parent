package test.model;

import org.igloo.storage.model.atomic.IFichierType;

public enum FichierType2 implements IFichierType {
  CONTENT3("content3"),
  CONTENT4("content4");

  private final String path;

  private FichierType2(String path) {
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
