package fr.openwide.core.showcase.web.application.widgets.component;

import java.util.Collections;
import java.util.List;

import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.odlabs.wiquery.ui.autocomplete.AutocompleteAjaxComponent;

import fr.openwide.core.jpa.exception.ServiceException;
import fr.openwide.core.showcase.core.business.user.model.User;
import fr.openwide.core.showcase.core.business.user.service.IUserService;
import fr.openwide.core.wicket.more.model.GenericEntityModel;

public class UserAutocompleteAjaxComponent extends AutocompleteAjaxComponent<User> {

	private static final long serialVersionUID = -4827520520856697565L;

	private static final UserChoiceRenderer USER_CHOICE_RENDERER = new UserChoiceRenderer();

	@SpringBean
	private IUserService userService;

	public UserAutocompleteAjaxComponent(String id) {
		this(id, new GenericEntityModel<Long, User>(new User()));
	}

	public UserAutocompleteAjaxComponent(String id, IModel<User> model) {
		super(id, model, USER_CHOICE_RENDERER);
	}

	@Override
	public List<User> getValues(String term) {
		try {
			return userService.searchAutocomplete(term, 10);
		} catch (ServiceException e) {
			return Collections.emptyList();
		}
	}

	@Override
	public User getValueOnSearchFail(String input) {
		return null;
	}

	private static class UserChoiceRenderer implements IChoiceRenderer<User> {
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
