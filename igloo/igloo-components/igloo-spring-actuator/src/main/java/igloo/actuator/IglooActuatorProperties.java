package igloo.actuator;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.StringUtils;

/**
 * Configure user/password security for actuators. From {@link
 * org.springframework.boot.security.autoconfigure.SecurityProperties} pattern.
 */
@ConfigurationProperties(prefix = "igloo.actuator.security")
public class IglooActuatorProperties {

  private final User actuator = new User();
  private final User prometheus = new User();

  public User getActuator() {
    return this.actuator;
  }

  public User getPrometheus() {
    return this.prometheus;
  }

  public static class User {

    /** Default user name. */
    private String name = "user";

    /** Password for the default user name. */
    private String password = UUID.randomUUID().toString();

    /** Granted roles for the default user name. */
    private List<String> roles = new ArrayList<>();

    private boolean passwordGenerated = true;

    public String getName() {
      return this.name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public String getPassword() {
      return this.password;
    }

    public void setPassword(String password) {
      if (!StringUtils.hasLength(password)) {
        return;
      }
      this.passwordGenerated = false;
      this.password = password;
    }

    public List<String> getRoles() {
      return this.roles;
    }

    public void setRoles(List<String> roles) {
      this.roles = new ArrayList<>(roles);
    }

    public boolean isPasswordGenerated() {
      return this.passwordGenerated;
    }
  }
}
