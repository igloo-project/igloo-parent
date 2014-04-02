package fr.openwide.core.basicapp.core.business.user.model;

public class UserSearchParameters {
	
	private final String name;
	
	private final UserGroup group;
	
	private final Boolean active;

	public UserSearchParameters(String name, UserGroup group, Boolean active) {
		super();
		this.name = name;
		this.group = group;
		this.active = active;
	}

	public String getName() {
		return name;
	}

	public UserGroup getGroup() {
		return group;
	}

	public Boolean getActive() {
		return active;
	}

}
