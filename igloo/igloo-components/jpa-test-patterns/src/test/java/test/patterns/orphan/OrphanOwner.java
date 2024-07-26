package test.patterns.orphan;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class OrphanOwner {

  @Id public Long id;

  /**
   * {@link CascadeType#ALL} is needed, else orphanRemoval is not done.
   *
   * <p>{@link OneToMany#orphanRemoval()} is exclusively triggered by modifying this collection.
   */
  @OneToMany(mappedBy = "owner", orphanRemoval = true, cascade = CascadeType.ALL)
  public List<OrphanItem> items = new ArrayList<>();
}
