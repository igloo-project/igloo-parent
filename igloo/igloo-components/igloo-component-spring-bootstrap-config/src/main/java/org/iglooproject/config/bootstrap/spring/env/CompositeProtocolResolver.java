package org.iglooproject.config.bootstrap.spring.env;

import java.util.ArrayList;
import java.util.List;
import org.springframework.core.io.ProtocolResolver;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

/** <b>Not part of public API ; do not use.</b> */
public class CompositeProtocolResolver implements ProtocolResolver {

  public static final String COMPOSITE_PREFIX = "composite:";

  @Override
  public Resource resolve(String location, ResourceLoader resourceLoader) {
    if (!location.startsWith(COMPOSITE_PREFIX)) {
      return null;
    }
    List<Resource> resources = new ArrayList<>();
    String splittedLocation = location.substring(COMPOSITE_PREFIX.length(), location.length());
    for (String item : splittedLocation.split(",")) {
      if (item != null && !item.isEmpty()) {
        Resource r = resourceLoader.getResource(item);
        if (r instanceof CompositeResource) {
          resources.addAll(((CompositeResource) r).getResources());
        } else {
          resources.add(r);
        }
      }
    }
    return new CompositeResource(resources);
  }
}
