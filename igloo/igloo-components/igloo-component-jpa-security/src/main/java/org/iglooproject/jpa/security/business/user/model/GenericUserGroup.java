package org.iglooproject.jpa.security.business.user.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Sets;
import igloo.hibernateconfig.api.HibernateSearchAnalyzer;
import igloo.hibernateconfig.api.HibernateSearchNormalizer;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.OrderBy;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.SortedSet;
import org.bindgen.Bindable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.JavaType;
import org.hibernate.annotations.SortComparator;
import org.hibernate.search.engine.backend.types.Sortable;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.KeywordField;
import org.hibernate.type.descriptor.java.StringJavaType;
import org.iglooproject.commons.util.collections.CollectionUtils;
import org.iglooproject.jpa.business.generic.model.GenericEntity;
import org.iglooproject.jpa.security.business.authority.model.Authority;
import org.iglooproject.jpa.security.business.user.util.GenericUserComparator;

@MappedSuperclass
@Bindable
public abstract class GenericUserGroup<
        G extends GenericUserGroup<G, U>, U extends GenericUser<U, G>>
    extends GenericEntity<Long, G> implements IUserGroup {

  private static final long serialVersionUID = 2156717229285615454L;

  public static final String NAME = "name";
  public static final String NAME_SORT = "nameSort";

  @Id @GeneratedValue private Long id;

  @Column
  @FullTextField(name = NAME, analyzer = HibernateSearchAnalyzer.TEXT)
  @KeywordField(
      name = NAME_SORT,
      normalizer = HibernateSearchNormalizer.TEXT,
      sortable = Sortable.YES)
  @SuppressWarnings("squid:S1845") // attribute name differs only by case on purpose
  private String name;

  /**
   * This field is here just to generate bindings (qGenericUserGroup).
   *
   * <p>It should not be accessed, since:
   *
   * <ol>
   *   <li>it is not kept up to date when adding a user to a group
   *   <li>loading it or keeping it up to date may lead to performance issues when groups contain
   *       many users (> 10k).
   * </ol>
   */
  @JsonIgnore
  @ManyToMany(mappedBy = "groups")
  @SortComparator(GenericUserComparator.class)
  private SortedSet<U> users = Sets.newTreeSet(GenericUserComparator.get()); // NOSONAR

  @JsonIgnore
  @ManyToMany
  @Cascade({CascadeType.SAVE_UPDATE})
  @OrderBy("name")
  @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
  private Set<Authority> authorities = new LinkedHashSet<>();

  @Column
  @JavaType(StringJavaType.class)
  private String description;

  @Column(nullable = false)
  private boolean locked = false;

  public GenericUserGroup() {}

  public GenericUserGroup(String name) {
    this.name = name;
  }

  protected abstract G thisAsConcreteType();

  @Override
  public Long getId() {
    return id;
  }

  @Override
  public void setId(Long id) {
    this.id = id;
  }

  @Override
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override
  public Set<Authority> getAuthorities() {
    return Collections.unmodifiableSet(authorities);
  }

  public void setAuthorities(Collection<Authority> authorities) {
    CollectionUtils.replaceAll(this.authorities, authorities);
  }

  public void addAuthority(Authority authority) {
    this.authorities.add(authority);
  }

  public void removeAuthority(Authority authority) {
    this.authorities.remove(authority);
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public boolean isLocked() {
    return locked;
  }

  public void setLocked(boolean locked) {
    this.locked = locked;
  }

  @Override
  public int compareTo(G group) {
    if (this == group) {
      return 0;
    }
    return STRING_COLLATOR_FRENCH.compare(this.getName(), group.getName());
  }
}
