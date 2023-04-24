package test.patterns.orphan;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class OrphanOwner {

	@Id
	public Long id;

	/**
	 * <p>{@link CascadeType#ALL} is needed, else orphanRemoval is not done.</p>
	 * <p>{@link OneToMany#orphanRemoval()} is exclusively triggered by modifying this collection.</p>
	 */
	@OneToMany(mappedBy = "owner", orphanRemoval = true, cascade = CascadeType.ALL)
	public List<OrphanItem> items = new ArrayList<>();

}
