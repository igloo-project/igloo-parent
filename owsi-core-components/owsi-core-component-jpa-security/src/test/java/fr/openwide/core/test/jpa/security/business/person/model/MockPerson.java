package fr.openwide.core.test.jpa.security.business.person.model;

import java.util.LinkedList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;

import org.hibernate.search.annotations.Indexed;

import fr.openwide.core.jpa.security.business.person.model.AbstractPerson;
import fr.openwide.core.jpa.security.business.person.model.IPersonGroup;

@Entity
@Indexed
public class MockPerson extends AbstractPerson<MockPerson> {
	private static final long serialVersionUID = 4396833928821998996L;

	@ManyToMany(mappedBy="persons")
	private List<MockPersonGroup> groups = new LinkedList<MockPersonGroup>();

	public List<MockPersonGroup> getGroups() {
		return groups;
	}

	public void setGroups(List<MockPersonGroup> groups) {
		this.groups = groups;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<IPersonGroup> getPersonGroups() {
		return (List<IPersonGroup>) ((Object) groups);
	}
}
