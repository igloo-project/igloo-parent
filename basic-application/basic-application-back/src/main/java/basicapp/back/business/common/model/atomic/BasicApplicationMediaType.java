package basicapp.back.business.common.model.atomic;

import org.iglooproject.commons.util.mime.IMediaType;
import org.iglooproject.commons.util.mime.MediaTypeDefinition;

public enum BasicApplicationMediaType implements IMediaType {
  ;

  private final MediaTypeDefinition definition;

  BasicApplicationMediaType(MediaTypeDefinition definition) {
    this.definition = definition;
  }

  @Override
  public MediaTypeDefinition definition() {
    return definition;
  }
}
