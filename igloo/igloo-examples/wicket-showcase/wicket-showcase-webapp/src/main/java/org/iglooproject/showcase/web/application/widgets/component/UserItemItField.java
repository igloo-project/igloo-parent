package org.iglooproject.showcase.web.application.widgets.component;

import java.util.List;
import java.util.Locale;

import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.iglooproject.showcase.core.business.user.model.User;
import org.iglooproject.showcase.core.business.user.search.IUserSearchQuery;
import org.iglooproject.showcase.core.business.user.service.IUserService;
import org.iglooproject.wicket.more.markup.html.template.js.jquery.plugins.itemit.ItemItField;

public class UserItemItField extends ItemItField<User, UserJson> {

	private static final long serialVersionUID = -7720627499673417965L;
	
	@SpringBean
	private IUserService userService;

	@SpringBean
	private IUserSearchQuery userSearchQuery;

	public UserItemItField(String id, IModel<List<User>> selectedItemListModel) {
		super(id, selectedItemListModel);
	}

	@Override
	public List<User> getValues(String term) {
		return userSearchQuery.nameAutocomplete(term).fullList();
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
