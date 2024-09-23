package org.iglooproject.jpa.security.business.user.model;

import static org.iglooproject.jpa.security.service.CoreJpaUserDetailsServiceImpl.EMPTY_PASSWORD_HASH;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.base.MoreObjects.ToStringHelper;
import igloo.hibernateconfig.api.HibernateSearchAnalyzer;
import igloo.hibernateconfig.api.HibernateSearchNormalizer;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import java.time.Instant;
import java.util.Locale;
import java.util.Objects;
import org.bindgen.Bindable;
import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.NaturalIdCache;
import org.hibernate.search.engine.backend.types.Sortable;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.GenericField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.KeywordField;
import org.iglooproject.jpa.business.generic.model.GenericEntity;

@Indexed
@MappedSuperclass
@Bindable
@NaturalIdCache
public abstract class GenericUser<U extends GenericUser<U>> extends GenericEntity<Long, U>
    implements IUser {

  private static final long serialVersionUID = 1803671157183603979L;

  public static final String ID = "id";

  public static final String USERNAME = "username";
  public static final String USERNAME_SORT = "usernameSort";

  public static final String ENABLED = "enabled";

  @Id @GeneratedValue private Long id;

  @Column(nullable = false)
  @NaturalId(mutable = true)
  @FullTextField(name = USERNAME, analyzer = HibernateSearchAnalyzer.TEXT)
  @KeywordField(
      name = USERNAME_SORT,
      normalizer = HibernateSearchNormalizer.TEXT,
      sortable = Sortable.YES)
  @SuppressWarnings("squid:S1845") // attribute name differs only by case on purpose
  private String username;

  @JsonIgnore private String passwordHash = EMPTY_PASSWORD_HASH;

  @Column(nullable = false)
  @GenericField(name = ENABLED)
  @SuppressWarnings("squid:S1845") // attribute name differs only by case on purpose
  private boolean enabled = true;

  @JsonIgnore
  @Column(nullable = false)
  private Instant creationDate;

  @JsonIgnore
  @Column(nullable = false)
  private Instant lastUpdateDate;

  @JsonIgnore @Column private Instant lastLoginDate;

  /** preferred locale for user, can be null */
  @JsonIgnore @Column private Locale locale;

  public GenericUser() {}

  public GenericUser(String username, String passwordHash) {
    this.username = username;
    this.passwordHash = passwordHash;
  }

  @Override
  public Long getId() {
    return id;
  }

  @Override
  public void setId(Long id) {
    this.id = id;
  }

  @Override
  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public abstract String getFullName();

  @Override
  public String getPasswordHash() {
    return passwordHash;
  }

  public void setPasswordHash(String passwordHash) {
    this.passwordHash = passwordHash;
  }

  public boolean hasPasswordHash() {
    return !Objects.equals(getPasswordHash(), EMPTY_PASSWORD_HASH);
  }

  @Override
  public boolean isEnabled() {
    return enabled;
  }

  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
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

  /**
   * Fournit la locale préférée de l'utilisateur. Il faut utiliser {@link
   * CoreConfigurer##toAvailableLocale(Locale)} si la locale préférée de l'utilisateur doit être
   * exploitée pour choisir des traductions. Cette méthode permet de mapper une locale quelconque
   * (incluant null) sur une locale qui sera obligatoirement reconnue pas le système (de manière à
   * avoir un fonctionnement prédictible).
   *
   * @return une locale, possiblement null
   */
  public Locale getLocale() {
    return locale;
  }

  public void setLocale(Locale locale) {
    this.locale = locale;
  }

  @Override
  public int compareTo(U user) {
    if (this.equals(user)) {
      return 0;
    }
    return STRING_COLLATOR_FRENCH.compare(this.getUsername(), user.getUsername());
  }

  @Override
  protected ToStringHelper toStringHelper() {
    return super.toStringHelper().add("username", getUsername());
  }
}
