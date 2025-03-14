package org.iglooproject.test.jpa.security.business.person.model;

import static org.iglooproject.jpa.security.service.CoreJpaUserDetailsServiceImpl.EMPTY_PASSWORD_HASH;

import jakarta.persistence.Basic;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import java.util.Locale;
import org.hibernate.annotations.NaturalId;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;
import org.iglooproject.jpa.business.generic.model.GenericEntity;
import org.iglooproject.jpa.security.business.user.model.IUser;

@Entity
@Indexed
public class MockUser extends GenericEntity<Long, MockUser> implements IUser {

  private static final long serialVersionUID = 4396833928821998996L;

  @Id @GeneratedValue private Long id;

  @Basic(optional = false)
  @NaturalId(mutable = true)
  private String username;

  @Basic private String passwordHash = EMPTY_PASSWORD_HASH;

  @Basic(optional = false)
  private String firstName;

  @Basic(optional = false)
  private String lastName;

  @Basic private String email;

  @Basic(optional = false)
  private boolean enabled = true;

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

  @Override
  public String getPasswordHash() {
    return passwordHash;
  }

  public void setPasswordHash(String passwordHash) {
    this.passwordHash = passwordHash;
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

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  @Override
  public Locale getLocale() {
    return Locale.FRENCH;
  }

  @Override
  public boolean isEnabled() {
    return enabled;
  }

  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }
}
