package basicapp.back.business.user.model;

import basicapp.back.business.role.model.Role;
import basicapp.back.business.role.model.comparator.RoleComparator;
import basicapp.back.business.user.model.atomic.UserType;
import basicapp.back.business.user.model.embeddable.UserAnnouncementInformation;
import basicapp.back.business.user.model.embeddable.UserPasswordInformation;
import basicapp.back.business.user.model.embeddable.UserPasswordRecoveryRequest;
import com.google.common.collect.Sets;
import jakarta.persistence.Basic;
import jakarta.persistence.Cacheable;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Transient;
import java.util.Collections;
import java.util.SortedSet;
import org.bindgen.Bindable;
import org.hibernate.annotations.SortComparator;
import org.hibernate.search.mapper.pojo.bridge.mapping.annotation.ValueBridgeRef;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.GenericField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;
import org.iglooproject.commons.util.collections.CollectionUtils;
import org.iglooproject.jpa.search.bridge.GenericEntityIdBridge;
import org.iglooproject.jpa.security.business.user.model.GenericSimpleUser;
import org.iglooproject.spring.util.StringUtils;

@Indexed
@Bindable
@Cacheable
@Inheritance(strategy = InheritanceType.JOINED)
@Entity(name = "user_")
public class User extends GenericSimpleUser<User> {

  private static final long serialVersionUID = 1508647513049577617L;

  public static final String TYPE = "TYPE";
  public static final String ROLES = "ROLES";

  @Basic(optional = false)
  @Enumerated(EnumType.STRING)
  @GenericField(name = TYPE)
  private UserType type;

  @ManyToMany(fetch = FetchType.LAZY)
  @SortComparator(RoleComparator.class)
  @GenericField(name = ROLES, valueBridge = @ValueBridgeRef(type = GenericEntityIdBridge.class))
  @JoinTable(indexes = @Index(name = "user__role_role_id_idx", columnList = "roles_id"))
  private SortedSet<Role> roles = Sets.newTreeSet(RoleComparator.get());

  @Embedded private UserPasswordInformation passwordInformation;

  @Embedded private UserPasswordRecoveryRequest passwordRecoveryRequest;

  @Embedded
  private UserAnnouncementInformation announcementInformation = new UserAnnouncementInformation();

  public User() {
    super();
  }

  public UserType getType() {
    return type;
  }

  public void setType(UserType type) {
    this.type = type;
  }

  public SortedSet<Role> getRoles() {
    return Collections.unmodifiableSortedSet(roles);
  }

  public void setRoles(SortedSet<Role> roles) {
    CollectionUtils.replaceAll(this.roles, roles);
  }

  public boolean addRole(Role role) {
    return roles.add(role);
  }

  public UserPasswordInformation getPasswordInformation() {
    if (passwordInformation == null) {
      passwordInformation = new UserPasswordInformation();
    }
    return passwordInformation;
  }

  public UserPasswordRecoveryRequest getPasswordRecoveryRequest() {
    if (passwordRecoveryRequest == null) {
      passwordRecoveryRequest = new UserPasswordRecoveryRequest();
    }
    return passwordRecoveryRequest;
  }

  public UserAnnouncementInformation getAnnouncementInformation() {
    if (announcementInformation == null) {
      announcementInformation = new UserAnnouncementInformation();
    }
    return announcementInformation;
  }

  public void setAnnouncementInformation(UserAnnouncementInformation announcementInformation) {
    this.announcementInformation = announcementInformation;
  }

  @Transient
  @Override
  public String getFullName() {
    String fullName = super.getFullName();
    if (StringUtils.hasText(fullName)) {
      return fullName;
    } else {
      return getEmail();
    }
  }
}
