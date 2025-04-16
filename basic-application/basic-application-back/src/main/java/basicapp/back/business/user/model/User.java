package basicapp.back.business.user.model;

import static org.iglooproject.jpa.security.service.CoreJpaUserDetailsServiceImpl.EMPTY_PASSWORD_HASH;

import basicapp.back.business.common.model.EmailAddress;
import basicapp.back.business.role.model.Role;
import basicapp.back.business.role.model.comparator.RoleComparator;
import basicapp.back.business.user.model.atomic.UserType;
import basicapp.back.business.user.model.embeddable.UserAnnouncementInformation;
import basicapp.back.business.user.model.embeddable.UserPasswordInformation;
import basicapp.back.business.user.model.embeddable.UserPasswordRecoveryRequest;
import basicapp.back.config.hibernate.search.bridge.EmailAddressValueBridge;
import basicapp.back.util.binding.Bindings;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.base.MoreObjects.ToStringHelper;
import com.google.common.collect.Sets;
import igloo.hibernateconfig.api.HibernateSearchAnalyzer;
import igloo.hibernateconfig.api.HibernateSearchNormalizer;
import jakarta.persistence.Basic;
import jakarta.persistence.Cacheable;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Transient;
import java.time.Instant;
import java.util.Collections;
import java.util.Locale;
import java.util.Objects;
import java.util.SortedSet;
import org.bindgen.Bindable;
import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.SortComparator;
import org.hibernate.search.engine.backend.types.Sortable;
import org.hibernate.search.mapper.pojo.bridge.mapping.annotation.ValueBridgeRef;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.GenericField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.KeywordField;
import org.iglooproject.commons.util.collections.CollectionUtils;
import org.iglooproject.functional.Joiners;
import org.iglooproject.jpa.business.generic.model.GenericEntity;
import org.iglooproject.jpa.search.bridge.GenericEntityIdBridge;
import org.iglooproject.jpa.security.business.user.model.IUser;
import org.iglooproject.mail.api.INotificationRecipient;
import org.iglooproject.spring.util.StringUtils;

@Indexed
@Bindable
@Cacheable
@Entity(name = "user_")
public class User extends GenericEntity<Long, User> implements IUser, INotificationRecipient {

  private static final long serialVersionUID = 1508647513049577617L;

  public static final String ID = "id";
  public static final String TYPE = "type";
  public static final String USERNAME = "username";
  public static final String USERNAME_AUTOCOMPLETE = USERNAME + "Autocomplete";
  public static final String FIRST_NAME = "firstName";
  public static final String FIRST_NAME_AUTOCOMPLETE = FIRST_NAME + "Autocomplete";
  public static final String LAST_NAME = "lastName";
  public static final String LAST_NAME_AUTOCOMPLETE = LAST_NAME + "Autocomplete";
  public static final String EMAIL_ADDRESS = "emailAddress";
  public static final String EMAIL_ADDRESS_AUTOCOMPLETE = EMAIL_ADDRESS + "Autocomplete";
  public static final String ROLES = "roles";
  public static final String ENABLED = "enabled";

  @Id @GeneratedValue private Long id;

  @Basic(optional = false)
  @Enumerated(EnumType.STRING)
  @GenericField(name = TYPE)
  private UserType type;

  @Basic(optional = false)
  @NaturalId(mutable = true)
  @KeywordField(
      name = USERNAME,
      normalizer = HibernateSearchNormalizer.TEXT,
      sortable = Sortable.YES)
  @FullTextField(name = USERNAME_AUTOCOMPLETE, analyzer = HibernateSearchAnalyzer.TEXT)
  private String username;

  @Basic @JsonIgnore private String passwordHash = EMPTY_PASSWORD_HASH;

  @Embedded private UserPasswordInformation passwordInformation;

  @Embedded private UserPasswordRecoveryRequest passwordRecoveryRequest;

  @Basic(optional = false)
  @KeywordField(
      name = FIRST_NAME,
      normalizer = HibernateSearchNormalizer.TEXT,
      sortable = Sortable.YES)
  @FullTextField(name = FIRST_NAME_AUTOCOMPLETE, analyzer = HibernateSearchAnalyzer.TEXT)
  private String firstName;

  @Basic(optional = false)
  @KeywordField(
      name = LAST_NAME,
      normalizer = HibernateSearchNormalizer.TEXT,
      sortable = Sortable.YES)
  @FullTextField(name = LAST_NAME_AUTOCOMPLETE, analyzer = HibernateSearchAnalyzer.TEXT)
  private String lastName;

  @Basic
  @FullTextField(
      name = EMAIL_ADDRESS_AUTOCOMPLETE,
      analyzer = HibernateSearchAnalyzer.TEXT,
      valueBridge = @ValueBridgeRef(type = EmailAddressValueBridge.class))
  private EmailAddress emailAddress;

  @Basic @JsonIgnore private Locale locale;

  @Basic(optional = false)
  @GenericField(name = ENABLED)
  private boolean enabled = true;

  @ManyToMany(fetch = FetchType.LAZY)
  @SortComparator(RoleComparator.class)
  @GenericField(name = ROLES, valueBridge = @ValueBridgeRef(type = GenericEntityIdBridge.class))
  @JoinTable(indexes = @Index(name = "user__role_role_id_idx", columnList = "roles_id"))
  private SortedSet<Role> roles = Sets.newTreeSet(RoleComparator.get());

  @Embedded
  private UserAnnouncementInformation announcementInformation = new UserAnnouncementInformation();

  @Basic(optional = false)
  @JsonIgnore
  private Instant creationDate;

  @Basic(optional = false)
  @JsonIgnore
  private Instant lastUpdateDate;

  @Basic @JsonIgnore private Instant lastLoginDate;

  public User() {
    super();
  }

  @Override
  public Long getId() {
    return id;
  }

  @Override
  public void setId(Long id) {
    this.id = id;
  }

  public UserType getType() {
    return type;
  }

  public void setType(UserType type) {
    this.type = type;
  }

  @Override
  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  @Override
  public String getPasswordHash() {
    return passwordHash;
  }

  public void setPasswordHash(String passwordHash) {
    this.passwordHash = passwordHash;
  }

  @Transient
  public boolean hasPasswordHash() {
    return !Objects.equals(getPasswordHash(), EMPTY_PASSWORD_HASH);
  }

  public UserPasswordInformation getPasswordInformation() {
    if (passwordInformation == null) {
      passwordInformation = new UserPasswordInformation();
    }
    return passwordInformation;
  }

  public void setPasswordInformation(UserPasswordInformation passwordInformation) {
    this.passwordInformation = passwordInformation;
  }

  public UserPasswordRecoveryRequest getPasswordRecoveryRequest() {
    if (passwordRecoveryRequest == null) {
      passwordRecoveryRequest = new UserPasswordRecoveryRequest();
    }
    return passwordRecoveryRequest;
  }

  public void setPasswordRecoveryRequest(UserPasswordRecoveryRequest passwordRecoveryRequest) {
    this.passwordRecoveryRequest = passwordRecoveryRequest;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  @Transient
  @Override
  public String getFullName() {
    return Joiners.onSpace()
        .join(
            StringUtils.emptyTextToNull(getFirstName()),
            StringUtils.emptyTextToNull(getLastName()));
  }

  public EmailAddress getEmailAddress() {
    return emailAddress;
  }

  public void setEmailAddress(EmailAddress emailAddress) {
    this.emailAddress = emailAddress;
  }

  @Override
  @Transient
  public String getNotificationEmailAddress() {
    return Bindings.emailAddress().value().apply(getEmailAddress());
  }

  /**
   * Fournit la locale préférée de l'utilisateur. Il faut utiliser {@link
   * CoreConfigurer##toAvailableLocale(Locale)} si la locale préférée de l'utilisateur doit être
   * exploitée pour choisir des traductions. Cette méthode permet de mapper une locale quelconque
   * (incluant null) sur une locale qui sera obligatoirement reconnue pas le système (de manière à
   * avoir un fonctionnement prédictible).
   *
   * @return une locale, possiblement null
   */
  @Override
  public Locale getLocale() {
    return locale;
  }

  public void setLocale(Locale locale) {
    this.locale = locale;
  }

  @Override
  public boolean isEnabled() {
    return enabled;
  }

  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }

  @Override
  public boolean isNotificationEnabled() {
    return isEnabled();
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

  public UserAnnouncementInformation getAnnouncementInformation() {
    if (announcementInformation == null) {
      announcementInformation = new UserAnnouncementInformation();
    }
    return announcementInformation;
  }

  public void setAnnouncementInformation(UserAnnouncementInformation announcementInformation) {
    this.announcementInformation = announcementInformation;
  }

  public Instant getCreationDate() {
    return creationDate;
  }

  public void setCreationDate(Instant creationDate) {
    this.creationDate = creationDate;
  }

  public Instant getLastUpdateDate() {
    return lastUpdateDate;
  }

  public void setLastUpdateDate(Instant lastUpdateDate) {
    this.lastUpdateDate = lastUpdateDate;
  }

  public Instant getLastLoginDate() {
    return lastLoginDate;
  }

  public void setLastLoginDate(Instant lastLoginDate) {
    this.lastLoginDate = lastLoginDate;
  }

  @Override
  protected ToStringHelper toStringHelper() {
    return super.toStringHelper().add("username", getUsername()).add("fullName", getFullName());
  }
}
