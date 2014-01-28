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

	@ManyToMany
	private List<MockPersonGroup> userGroups = new LinkedList<MockPersonGroup>();

	public List<MockPersonGroup> getUserGroups() {
		return userGroups;
	}

	public void setUserGroups(List<MockPersonGroup> userGroups) {
		this.userGroups = userGroups;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<IPersonGroup> getPersonGroups() {
		return (List<IPersonGroup>) ((Object) userGroups);
	}
	
	@Override
	public void addPersonGroup(IPersonGroup personGroup) {
		if (personGroup instanceof MockPersonGroup) {
			this.userGroups.add((MockPersonGroup) personGroup);
		}
	}
	
	@Override
	public void removePersonGroup(IPersonGroup personGroup) {
		this.userGroups.remove(personGroup);
	}

}
