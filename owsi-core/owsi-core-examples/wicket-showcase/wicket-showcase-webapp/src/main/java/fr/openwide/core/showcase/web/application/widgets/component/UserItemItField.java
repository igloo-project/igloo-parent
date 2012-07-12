package fr.openwide.core.showcase.web.application.widgets.component;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import fr.openwide.core.jpa.exception.ServiceException;
import fr.openwide.core.showcase.core.business.user.model.User;
import fr.openwide.core.showcase.core.business.user.service.IUserService;
import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.itemit.ItemItField;

public class UserItemItField extends ItemItField<User, UserJson> {

	private static final long serialVersionUID = -7720627499673417965L;

	@SpringBean
	private IUserService userService;

	public UserItemItField(String id, IModel<List<User>> selectedItemListModel) {
		super(id, selectedItemListModel);
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
	public UserJson newAutocompleteJson(Integer index, User item, Locale locale) {
		return new UserJson(item);
	}

	@Override
	public User convertValue(UserJson value) {
		return userService.getById(value.getId());
	}

	@Override
	public Class<UserJson> getJsonType() {
		return UserJson.class;
	}

}
