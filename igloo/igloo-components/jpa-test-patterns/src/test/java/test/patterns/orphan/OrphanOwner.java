package test.patterns.orphan;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

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
