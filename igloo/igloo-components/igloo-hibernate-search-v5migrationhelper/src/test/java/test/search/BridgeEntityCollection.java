package test.search;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.search.engine.backend.types.Projectable;
import org.hibernate.search.mapper.pojo.bridge.mapping.annotation.ValueBridgeRef;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.GenericField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;
import org.iglooproject.jpa.business.generic.model.GenericEntity;
import org.iglooproject.jpa.search.bridge.GenericEntityIdBridge;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;

@Entity
@Indexed
public class BridgeEntityCollection extends GenericEntity<Long, BridgeEntityCollection> {

	private static final long serialVersionUID = 5968050124765460930L;

	public BridgeEntityCollection() {}

	@Id
	@GeneratedValue
	private Long id;

	@ManyToMany
	@GenericField(valueBridge = @ValueBridgeRef(type = GenericEntityIdBridge.class), projectable = Projectable.YES)
	public List<BridgeLinkedEntity> linked = new ArrayList<>();

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

}
