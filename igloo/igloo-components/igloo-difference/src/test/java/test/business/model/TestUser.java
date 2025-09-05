package test.business.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import org.iglooproject.jpa.business.generic.model.GenericEntity;

@Entity
public class TestUser extends GenericEntity<Long, TestUser> {
  private static final long serialVersionUID = 1L;

  @Id @GeneratedValue private Long id;

  public TestUser(Long id) {
    this.id = id;
  }

  @Override
  public Long getId() {
    return id;
  }

  @Override
  public void setId(Long id) {
    this.id = id;
  }
}
