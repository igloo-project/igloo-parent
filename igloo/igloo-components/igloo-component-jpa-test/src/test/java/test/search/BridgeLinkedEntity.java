package test.search;

import org.iglooproject.jpa.business.generic.model.GenericEntity;
import org.iglooproject.jpa.business.generic.model.GenericEntityReference;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class BridgeLinkedEntity extends GenericEntity<Long, BridgeLinkedEntity> {

	private static final long serialVersionUID = 5968050124765460930L;

	public BridgeLinkedEntity() {}

	@Id
	@GeneratedValue
	private Long id;

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	@Embeddable
	public static class Reference extends GenericEntityReference<Long, BridgeLinkedEntity> {
		private static final long serialVersionUID = -1L;
		
		public Reference() {
			super();
		}
		public Reference(BridgeLinkedEntity entity) {
			super(entity);
		}
		public Reference(Class<? extends BridgeLinkedEntity> entityClass, Long entityId) {
			super(entityClass, entityId);
		}
	}

}
