package org.iglooproject.basicapp.core.security.model;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.function.Consumer;
import org.iglooproject.commons.util.collections.CollectionUtils;
import org.iglooproject.jpa.security.password.rule.SecurityPasswordRulesBuilder;
import org.passay.Rule;

public final class SecurityOptions {

  private final SecurityOptionsMode passwordExpiration;
  private final SecurityOptionsMode passwordHistory;
  private final SecurityOptionsMode passwordAdminUpdate;
  private final SecurityOptionsMode passwordAdminRecovery;
  private final SecurityOptionsMode passwordUserUpdate;
  private final SecurityOptionsMode passwordUserRecovery;

  private final Set<Rule> passwordRules;

  private SecurityOptions(Builder builder) {
    this.passwordExpiration = builder.passwordExpiration;
    this.passwordHistory = builder.passwordHistory;
    this.passwordAdminUpdate = builder.passwordAdminUpdate;
    this.passwordAdminRecovery = builder.passwordAdminRecovery;
    this.passwordUserUpdate = builder.passwordUserUpdate;
    this.passwordUserRecovery = builder.passwordUserRecovery;
    this.passwordRules = ImmutableSet.copyOf(builder.passwordRules);
  }

  public SecurityOptionsMode getPasswordExpiration() {
    return passwordExpiration;
  }

  public boolean isPasswordExpirationEnabled() {
    return SecurityOptionsMode.ENABLED.equals(getPasswordExpiration());
  }

  public SecurityOptionsMode getPasswordHistory() {
    return passwordHistory;
  }

  public boolean isPasswordHistoryEnabled() {
    return SecurityOptionsMode.ENABLED.equals(getPasswordHistory());
  }

  public SecurityOptionsMode getPasswordUserUpdate() {
    return passwordUserUpdate;
  }

  public boolean isPasswordUserUpdateEnabled() {
    return SecurityOptionsMode.ENABLED.equals(getPasswordUserUpdate());
  }

  public SecurityOptionsMode getPasswordAdminUpdate() {
    return passwordAdminUpdate;
  }

  public boolean isPasswordAdminUpdateEnabled() {
    return SecurityOptionsMode.ENABLED.equals(getPasswordAdminUpdate())
        || SecurityOptionsMode.DISABLED.equals(getPasswordUserRecovery());
  }

  public SecurityOptionsMode getPasswordUserRecovery() {
    return passwordUserRecovery;
  }

  public boolean isPasswordUserRecoveryEnabled() {
    return SecurityOptionsMode.ENABLED.equals(getPasswordUserRecovery());
  }

  public SecurityOptionsMode getPasswordAdminRecovery() {
    return passwordAdminRecovery;
  }

  public boolean isPasswordAdminRecoveryEnabled() {
    return SecurityOptionsMode.ENABLED.equals(getPasswordAdminRecovery());
  }

  public Set<Rule> getPasswordRules() {
    return Collections.unmodifiableSet(passwordRules);
  }

  public static final SecurityOptions create(Consumer<Builder> consumer) {
    Builder builder = new Builder();
    consumer.accept(builder);
    return builder.build();
  }

  public static final class Builder {

    private SecurityOptionsMode passwordExpiration = SecurityOptionsMode.DISABLED;
    private SecurityOptionsMode passwordHistory = SecurityOptionsMode.DISABLED;
    private SecurityOptionsMode passwordAdminUpdate = SecurityOptionsMode.DISABLED;
    private SecurityOptionsMode passwordAdminRecovery = SecurityOptionsMode.DISABLED;
    private SecurityOptionsMode passwordUserUpdate = SecurityOptionsMode.DISABLED;
    private SecurityOptionsMode passwordUserRecovery = SecurityOptionsMode.DISABLED;

    private Set<Rule> passwordRules = Sets.newHashSet();

    private Builder() {}

    private SecurityOptions build() {
      return new SecurityOptions(this);
    }

    public Builder passwordExpiration() {
      this.passwordExpiration = SecurityOptionsMode.ENABLED;
      return this;
    }

    public Builder passwordHistory() {
      this.passwordHistory = SecurityOptionsMode.ENABLED;
      return this;
    }

    public Builder passwordAdminUpdate() {
      this.passwordAdminUpdate = SecurityOptionsMode.ENABLED;
      return this;
    }

    public Builder passwordAdminRecovery() {
      this.passwordAdminRecovery = SecurityOptionsMode.ENABLED;
      return this;
    }

    public Builder passwordUserUpdate() {
      this.passwordUserUpdate = SecurityOptionsMode.ENABLED;
      return this;
    }

    public Builder passwordUserRecovery() {
      this.passwordUserRecovery = SecurityOptionsMode.ENABLED;
      return this;
    }

    public Builder passwordRules(Collection<Rule> passwordRules) {
      CollectionUtils.replaceAll(this.passwordRules, passwordRules);
      return this;
    }

    public Builder passwordRules(Consumer<SecurityPasswordRulesBuilder> consumer) {
      CollectionUtils.replaceAll(this.passwordRules, SecurityPasswordRulesBuilder.create(consumer));
      return this;
    }
  }

  private enum SecurityOptionsMode {
    ENABLED,
    DISABLED;
  }
}
