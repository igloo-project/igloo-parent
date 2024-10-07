package basicapp.back.business.role.model;

import basicapp.back.business.user.model.User;
import com.google.common.collect.Sets;
import jakarta.persistence.Basic;
import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import java.util.Collections;
import java.util.Set;
import java.util.SortedSet;
import org.bindgen.Bindable;
import org.hibernate.Length;
import org.iglooproject.commons.util.collections.CollectionUtils;
import org.iglooproject.jpa.business.generic.model.GenericEntity;

@Entity
@Bindable
@Cacheable
public class Role extends GenericEntity<Long, Role> {

  private static final long serialVersionUID = 1L;

  @Id @GeneratedValue private Long id;

  @Basic(optional = false)
  @Column(unique = true, length = Length.DEFAULT)
  private String title;

  @ElementCollection private Set<String> permissions = Sets.newHashSet();

  // DO NOT USE - QueryDSL association inverse side.
  @ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
  private SortedSet<User> users;

  @Override
  public Long getId() {
    return id;
  }

  @Override
  public void setId(Long id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public Set<String> getPermissions() {
    return Collections.unmodifiableSet(permissions);
  }

  public void setPermissions(Set<String> permissions) {
    CollectionUtils.replaceAll(this.permissions, permissions);
  }
}
