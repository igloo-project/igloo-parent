package org.igloo.storage.tools.util;

import org.apache.commons.lang3.EnumUtils;
import org.igloo.storage.model.atomic.IStorageUnitType;
import picocli.CommandLine.ITypeConverter;
import picocli.CommandLine.TypeConversionException;

public class StorageUnitTypeConverter implements ITypeConverter<IStorageUnitType> {

  @Override
  public IStorageUnitType convert(String value) {
    int dotLastIndex = value.lastIndexOf(".");
    if (dotLastIndex == -1) {
      throw conversionException(value);
    }
    String className = value.substring(0, dotLastIndex);
    String enumValue = value.substring(dotLastIndex + 1);

    try {
      @SuppressWarnings("rawtypes")
      Class clazz = Class.forName(className);
      @SuppressWarnings("unchecked")
      Object candidate = EnumUtils.getEnum(clazz, enumValue);
      if (candidate instanceof IStorageUnitType storageUnitType) {
        return storageUnitType;
      } else {
        throw conversionException(value);
      }
    } catch (ClassNotFoundException e) {
      throw conversionException(enumValue, e);
    }
  }

  protected TypeConversionException conversionException(String value) {
    return conversionException(value, null);
  }

  protected TypeConversionException conversionException(String value, Throwable cause) {
    var exc =
        new TypeConversionException(
            "%s cannot be mapped to a IStorageUnitType. Use org.package.ClassName.VALUE syntax"
                .formatted(value));
    if (cause != null) {
      exc.initCause(cause);
    }
    return exc;
  }
}
