package org.iglooproject.jpa.security.business.user.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import igloo.hibernateconfig.api.HibernateSearchAnalyzer;
import igloo.hibernateconfig.api.HibernateSearchNormalizer;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Transient;
import java.util.SortedSet;
import org.bindgen.Bindable;
import org.hibernate.search.engine.backend.types.Sortable;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.KeywordField;
import org.iglooproject.functional.Joiners;
import org.iglooproject.jpa.business.generic.model.GenericEntity;
import org.iglooproject.mail.api.INotificationRecipient;
import org.iglooproject.spring.util.StringUtils;

@MappedSuperclass
@Bindable
public abstract class GenericSimpleUser<
        U extends GenericSimpleUser<U, G>, G extends GenericUserGroup<G, U>>
    extends GenericUser<U, G> implements ISimpleUser, INotificationRecipient {

  private static final long serialVersionUID = 4869548461178261021L;

  public static final String FIRST_NAME = "firstName";
  public static final String FIRST_NAME_SORT = "firstNameSort";

  public static final String LAST_NAME = "lastName";
  public static final String LAST_NAME_SORT = "lastNameSort";

  public static final String EMAIL = "email";

  @Column(nullable = false)
  @FullTextField(name = FIRST_NAME, analyzer = HibernateSearchAnalyzer.TEXT)
  @KeywordField(
      name = FIRST_NAME_SORT,
      normalizer = HibernateSearchNormalizer.TEXT,
      sortable = Sortable.YES)
  private String firstName;

  @Column(nullable = false)
  @FullTextField(name = LAST_NAME, analyzer = HibernateSearchAnalyzer.TEXT)
  @KeywordField(
      name = LAST_NAME_SORT,
      normalizer = HibernateSearchNormalizer.TEXT,
      sortable = Sortable.YES)
  private String lastName;

  @Column
  @FullTextField(name = EMAIL, analyzer = HibernateSearchAnalyzer.TEXT)
  @SuppressWarnings("squid:S1845") // attribute name differs only by case on purpose
  private String email;

  public GenericSimpleUser() {
    super();
  }

  public GenericSimpleUser(
      String username, String firstName, String lastName, String passwordHash) {
    super(username, passwordHash);
    setFirstName(firstName);
    setLastName(lastName);
  }

  /*
   * Works around a bindgen bug, where bindgen seems unable to substitute a concrete type to the "G" type parameter if we don't override this method here.
   */
  @Override
  public SortedSet<G> getGroups() {
    return super.getGroups(); // NOSONAR
  }

  @Override
  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  @Override
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

  @Override
  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  @Override
  public int compareTo(U user) {
    if (this.equals(user)) {
      return 0;
    }

    if (GenericEntity.STRING_COLLATOR_FRENCH.compare(this.getLastName(), user.getLastName()) == 0) {
      return STRING_COLLATOR_FRENCH.compare(this.getFirstName(), user.getFirstName());
    }
    return STRING_COLLATOR_FRENCH.compare(this.getLastName(), user.getLastName());
  }

  @Override
  @Transient
  @JsonIgnore
  public boolean isNotificationEnabled() {
    // implémentation par défaut ; dépend de l'état de l'utilisateur
    return isEnabled();
  }
}
