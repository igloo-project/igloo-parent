package org.igloo.spring.autoconfigure.security;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "security")
public class SecurityProperties {

  private String runAsKey;

  public String getRunAsKey() {
    return runAsKey;
  }

  public void setRunAsKey(String runAsKey) {
    this.runAsKey = runAsKey;
  }
}
