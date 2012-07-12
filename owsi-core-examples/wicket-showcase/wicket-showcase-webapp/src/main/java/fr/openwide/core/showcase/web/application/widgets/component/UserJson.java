package fr.openwide.core.showcase.web.application.widgets.component;

import java.io.Serializable;

import fr.openwide.core.showcase.core.business.user.model.User;

public class UserJson implements Serializable {

	private static final long serialVersionUID = -8345560031721147877L;

	private Long id;

	private String label;

	protected UserJson() {
		
	}

	public UserJson(User user) {
		this();
		
		this.label = user.getDisplayName();
		this.id = user.getId();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
}
