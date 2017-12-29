package org.iglooproject.showcase.web.application.widgets.component;

import java.util.List;

import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.model.IModel;

import org.iglooproject.showcase.core.business.user.model.User;
import org.iglooproject.showcase.core.business.user.search.IUserSearchQuery;
import org.iglooproject.wicket.more.markup.html.form.AutocompleteAjaxComponent;
import org.iglooproject.wicket.more.model.GenericEntityModel;

public class UserAutocompleteAjaxComponent extends AutocompleteAjaxComponent<User> {

	private static final long serialVersionUID = -4827520520856697565L;

	private static final UserChoiceRenderer USER_CHOICE_RENDERER = new UserChoiceRenderer();

	public UserAutocompleteAjaxComponent(String id) {
		this(id, new GenericEntityModel<Long, User>(new User()));
	}

	public UserAutocompleteAjaxComponent(String id, IModel<User> model) {
		super(id, model, USER_CHOICE_RENDERER);
	}

	@Override
	public List<User> getValues(String term) {
		return getBean(IUserSearchQuery.class).nameAutocomplete(term).fullList();
	}

	@Override
	public User getValueOnSearchFail(String input) {
		return null;
	}

	private static class UserChoiceRenderer extends ChoiceRenderer<User> {
		private static final long serialVersionUID = 1L;
		
		@Override
		public Object getDisplayValue(User user) {
			return user != null ? user.getFullName() : "";
		}
		
		@Override
		public String getIdValue(User user, int index) {
			return user != null ? String.valueOf(user.getId()) : "-1";
		}
	}
}
