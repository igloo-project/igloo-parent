package fr.openwide.core.basicapp.web.application.administration.component;

import org.apache.wicket.model.IModel;

import fr.openwide.core.basicapp.core.business.user.model.UserGroup;
import fr.openwide.core.wicket.markup.html.panel.GenericPanel;

public class UserGroupMembersPanel extends GenericPanel<UserGroup> {

	private static final long serialVersionUID = 1955579250974258074L;

	public UserGroupMembersPanel(String id, IModel<? extends UserGroup> model) {
		super(id, model);
	}
}
