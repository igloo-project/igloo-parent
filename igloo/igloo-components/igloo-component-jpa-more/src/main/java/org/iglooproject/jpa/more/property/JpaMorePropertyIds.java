package org.iglooproject.jpa.more.property;

import java.io.File;
import org.iglooproject.spring.property.model.AbstractPropertyIds;
import org.iglooproject.spring.property.model.ImmutablePropertyId;
import org.iglooproject.spring.property.model.MutablePropertyId;

public class JpaMorePropertyIds extends AbstractPropertyIds {

  private JpaMorePropertyIds() {}

  public static final MutablePropertyId<Boolean> DATABASE_INITIALIZED =
      mutable("databaseInitialized");

  public static final MutablePropertyId<Boolean> MAINTENANCE = mutable("maintenance");

  public static final ImmutablePropertyId<File> IMAGE_MAGICK_CONVERT_BINARY_PATH =
      immutable("imageMagick.convertBinary.path");
}
