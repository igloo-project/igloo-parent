package fr.openwide.core.basicapp.web.application.administration.form;

import java.util.List;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

import fr.openwide.core.basicapp.core.business.user.model.UserGroup;
import fr.openwide.core.basicapp.core.business.user.search.IUserGroupSearchQuery;
import fr.openwide.core.basicapp.web.application.common.form.impl.UserGroupChoiceRenderer;
import fr.openwide.core.wicket.more.application.CoreWicketApplication;
import fr.openwide.core.wicket.more.markup.html.select2.GenericSelect2DropDownSingleChoice;

public class UserGroupDropDownSingleChoice extends GenericSelect2DropDownSingleChoice<UserGroup> {
	
	private static final long serialVersionUID = 8845987968858565378L;
	
	public UserGroupDropDownSingleChoice(String id, IModel<UserGroup> model) {
		super(id, model, new ChoicesModel(), UserGroupChoiceRenderer.get());
	}
	
	private static class ChoicesModel extends LoadableDetachableModel<List<UserGroup>> {
		
		private static final long serialVersionUID = 1L;
		
		@Override
		protected List<UserGroup> load() {
			return CoreWicketApplication.get().getApplicationContext().getBean(IUserGroupSearchQuery.class)
					.fullList();
		}
	}
}
