package test.patterns.orphan;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class OrphanItem {

	@Id
	public Long id;

	/**
	 * <p>This sides owns the relationship (owner_id field), but orphanRemoval is triggered when this entity is
	 * removed from its {@link OrphanOwner#items} collection.</p>
	 */
	@ManyToOne
	public OrphanOwner owner;

}
