package test.search;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import org.iglooproject.jpa.business.generic.model.GenericEntity;

@Entity
public class BridgeLinkedEntity extends GenericEntity<Long, BridgeLinkedEntity> {

  private static final long serialVersionUID = 5968050124765460930L;

  public BridgeLinkedEntity() {}

  @Id @GeneratedValue private Long id;

  @Override
  public Long getId() {
    return id;
  }

  @Override
  public void setId(Long id) {
    this.id = id;
  }
}
