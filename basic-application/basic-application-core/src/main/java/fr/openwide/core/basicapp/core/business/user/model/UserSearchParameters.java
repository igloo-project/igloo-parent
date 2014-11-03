package fr.openwide.core.basicapp.core.business.user.model;

public class UserSearchParameters {
	
	private final String name;
	
	private final UserGroup group;
	
	private final Boolean includeInactives;

	public UserSearchParameters(String name, UserGroup group, Boolean includeInactives) {
		super();
		this.name = name;
		this.group = group;
		this.includeInactives = includeInactives;
	}

	public String getName() {
		return name;
	}

	public UserGroup getGroup() {
		return group;
	}

	public Boolean getIncludeInactives() {
		return includeInactives;
	}

}
