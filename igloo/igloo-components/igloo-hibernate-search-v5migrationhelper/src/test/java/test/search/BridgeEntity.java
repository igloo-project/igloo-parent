package test.search;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import org.hibernate.search.engine.backend.types.Projectable;
import org.hibernate.search.engine.backend.types.Sortable;
import org.hibernate.search.mapper.pojo.bridge.mapping.annotation.ValueBridgeRef;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.GenericField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;
import org.iglooproject.jpa.business.generic.model.GenericEntity;
import org.iglooproject.jpa.search.bridge.GenericEntityIdBridge;
import org.iglooproject.jpa.search.bridge.GenericEntityReferenceIdBridge;

@Entity
@Indexed
public class BridgeEntity extends GenericEntity<Long, BridgeEntity> {

  private static final long serialVersionUID = 5968050124765460930L;

  public BridgeEntity() {}

  @Id @GeneratedValue private Long id;

  @ManyToOne
  @GenericField(
      valueBridge = @ValueBridgeRef(type = GenericEntityIdBridge.class),
      projectable = Projectable.YES)
  public BridgeLinkedEntity linked;

  @ManyToOne
  @GenericField(
      valueBridge = @ValueBridgeRef(type = GenericEntityIdBridge.class),
      sortable = Sortable.YES)
  public BridgeLinkedEntity linked2;

  @Embedded
  @GenericField(
      valueBridge = @ValueBridgeRef(type = GenericEntityReferenceIdBridge.class),
      projectable = Projectable.YES)
  public BridgeLinkedEntity.Reference reference;

  @Override
  public Long getId() {
    return id;
  }

  @Override
  public void setId(Long id) {
    this.id = id;
  }
}
