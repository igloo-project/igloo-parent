package org.iglooproject.config.bootstrap.spring.env;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import org.springframework.core.io.Resource;

/** <b>Not part of public API ; do not use.</b> */
class CompositeResource implements Resource {

  public final List<Resource> resources;

  public CompositeResource(List<Resource> resources) {
    this.resources = new ArrayList<>(resources);
  }

  public List<Resource> getResources() {
    return resources;
  }

  @Override
  public InputStream getInputStream() throws IOException {
    throw new IllegalStateException();
  }

  @Override
  public boolean exists() {
    throw new IllegalStateException();
  }

  @Override
  public URL getURL() throws IOException {
    throw new IllegalStateException();
  }

  @Override
  public URI getURI() throws IOException {
    throw new IllegalStateException();
  }

  @Override
  public File getFile() throws IOException {
    throw new IllegalStateException();
  }

  @Override
  public long contentLength() throws IOException {
    throw new IllegalStateException();
  }

  @Override
  public long lastModified() throws IOException {
    throw new IllegalStateException();
  }

  @Override
  public Resource createRelative(String relativePath) throws IOException {
    throw new IllegalStateException();
  }

  @Override
  public String getFilename() {
    throw new IllegalStateException();
  }

  @Override
  public String getDescription() {
    return "bootstrap configuration resource";
  }
}
