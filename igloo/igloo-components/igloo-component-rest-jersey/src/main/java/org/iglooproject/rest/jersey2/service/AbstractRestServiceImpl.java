package org.iglooproject.rest.jersey2.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.iglooproject.rest.jersey2.util.exception.CoreRemoteApiError;
import org.iglooproject.rest.jersey2.util.exception.IRemoteApiError;
import org.iglooproject.rest.jersey2.util.exception.RemoteApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

public abstract class AbstractRestServiceImpl {

  private static final Logger LOGGER = LoggerFactory.getLogger(AbstractRestServiceImpl.class);

  private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

  protected RemoteApiException getException(IRemoteApiError error) {
    return new RemoteApiException(error);
  }

  protected RemoteApiException getException(IRemoteApiError error, Throwable cause) {
    LOGGER.error(error.getCode() + " - " + error.getMessage(), cause);

    return new RemoteApiException(error, cause);
  }

  protected <K, V> Map<K, V> getMapFromJsonString(
      String jsonString, Class<K> keyClass, Class<V> valueClass) {
    try {
      return OBJECT_MAPPER.readValue(
          jsonString,
          OBJECT_MAPPER
              .getTypeFactory()
              .constructMapType(
                  LinkedHashMap.class,
                  OBJECT_MAPPER.constructType(keyClass),
                  OBJECT_MAPPER.constructType(valueClass)));
    } catch (RuntimeException | IOException e) {
      LOGGER.error(CoreRemoteApiError.UNSERIALIZATION_ERROR.getMessage(), e);

      throw (getException(CoreRemoteApiError.UNSERIALIZATION_ERROR, e));
    }
  }

  protected <V> List<V> getListFromJsonString(String jsonString, Class<V> valueClass) {
    try {
      return OBJECT_MAPPER.readValue(
          jsonString,
          OBJECT_MAPPER
              .getTypeFactory()
              .constructCollectionType(ArrayList.class, OBJECT_MAPPER.constructType(valueClass)));
    } catch (RuntimeException | IOException e) {
      LOGGER.error(CoreRemoteApiError.UNSERIALIZATION_ERROR.getMessage(), e);

      throw (getException(CoreRemoteApiError.UNSERIALIZATION_ERROR, e));
    }
  }

  protected <V> V getObjectFromJsonString(String jsonString, Class<V> valueClass) {
    try {
      return OBJECT_MAPPER.readValue(jsonString, valueClass);
    } catch (RuntimeException | IOException e) {
      LOGGER.error("Unserialization error", e);

      throw (getException(CoreRemoteApiError.UNSERIALIZATION_ERROR, e));
    }
  }

  protected Map<String, File> getFileMapFromFormDataBodyPartList(List<FormDataBodyPart> parts) {
    Map<String, File> files = new LinkedHashMap<>();

    if (parts != null) {
      for (FormDataBodyPart part : parts) {
        String fileName = part.getContentDisposition().getFileName();
        File file = part.getValueAs(File.class);

        if (StringUtils.hasText(fileName) && file != null && file.canRead()) {
          files.put(fileName, file);
        }
      }
    }

    return files;
  }

  protected File getFileFromFormDataBodyPart(FormDataBodyPart part) {
    File file = part.getValueAs(File.class);
    if (file != null && file.canRead()) {
      return file;
    } else {
      return null;
    }
  }
}
