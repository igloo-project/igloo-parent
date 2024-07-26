package org.iglooproject.wicket.more.export.model;

import java.io.File;
import org.apache.wicket.model.IModel;
import org.javatuples.LabelValue;

public abstract class AbstractFileGenerationModel implements IModel<LabelValue<String, File>> {

  private static final long serialVersionUID = -2527766488833656503L;

  private final IModel<String> fileNameModel;

  public AbstractFileGenerationModel(IModel<String> fileNameModel) {
    this.fileNameModel = fileNameModel;
  }

  @Override
  public LabelValue<String, File> getObject() {
    return LabelValue.with(fileNameModel.getObject(), generateFile());
  }

  protected abstract File generateFile();
}
