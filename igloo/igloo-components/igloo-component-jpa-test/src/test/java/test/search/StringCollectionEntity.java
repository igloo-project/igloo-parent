package test.search;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.search.engine.backend.types.Projectable;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.GenericField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
@Indexed
public class StringCollectionEntity {

	@Id
	@GeneratedValue
	private Long id;

	@ElementCollection
	@GenericField(projectable = Projectable.YES)
	public List<String> strings = new ArrayList<>();
}
