package org.iglooproject.basicapp.core.business.user.typedescriptor;

import java.io.Serializable;
import java.util.Objects;
import org.bindgen.Bindable;
import org.danekja.java.util.function.serializable.SerializableSupplier;
import org.iglooproject.basicapp.core.business.user.model.BasicUser;
import org.iglooproject.basicapp.core.business.user.model.TechnicalUser;
import org.iglooproject.basicapp.core.business.user.model.User;
import org.iglooproject.basicapp.core.util.ResourceKeyGenerator;

@Bindable
public abstract class UserTypeDescriptor<U extends User> implements Serializable {

  private static final long serialVersionUID = -5304177464509061289L;

  public static final UserTypeDescriptor<TechnicalUser> TECHNICAL_USER =
      new UserTypeDescriptor<TechnicalUser>(
          TechnicalUser.class, () -> new TechnicalUser(), "technicalUser") {
        private static final long serialVersionUID = 8001217632966359567L;
      };

  public static final UserTypeDescriptor<BasicUser> BASIC_USER =
      new UserTypeDescriptor<BasicUser>(BasicUser.class, () -> new BasicUser(), "basicUser") {
        private static final long serialVersionUID = 6944866921473743053L;
      };

  public static final UserTypeDescriptor<User> USER =
      new UserTypeDescriptor<User>(User.class, () -> new User(), "user") {
        private static final long serialVersionUID = 1959441675935327099L;
      };

  @SuppressWarnings("unchecked")
  public static final <U extends User> UserTypeDescriptor<U> get(U user) {
    if (user == null) {
      return null;
    }
    return (UserTypeDescriptor<U>) get(user.getClass());
  }

  @SuppressWarnings("unchecked")
  public static final <U extends User> UserTypeDescriptor<U> get(Class<? extends U> clazz) {
    if (TechnicalUser.class.isAssignableFrom(clazz)) {
      return (UserTypeDescriptor<U>) TECHNICAL_USER;
    } else if (BasicUser.class.isAssignableFrom(clazz)) {
      return (UserTypeDescriptor<U>) BASIC_USER;
    }
    return (UserTypeDescriptor<U>) USER;
  }

  private final Class<U> clazz;

  private final SerializableSupplier<U> supplier;

  private final String resourceKeyBase;

  protected UserTypeDescriptor(
      Class<U> clazz, SerializableSupplier<U> supplier, String resourceKeyBase) {
    this.clazz = Objects.requireNonNull(clazz);
    this.supplier = Objects.requireNonNull(supplier);
    this.resourceKeyBase = Objects.requireNonNull(resourceKeyBase);
  }

  public Class<U> getClazz() {
    return clazz;
  }

  public SerializableSupplier<U> getSupplier() {
    return supplier;
  }

  public String getResourceKeyBase() {
    return resourceKeyBase;
  }

  public ResourceKeyGenerator getResourceKeyGenerator() {
    return ResourceKeyGenerator.of(getResourceKeyBase());
  }

  public String resourceKey(String prefix, String suffix) {
    return getResourceKeyGenerator().prepend(prefix).append(suffix).resourceKey();
  }
}
