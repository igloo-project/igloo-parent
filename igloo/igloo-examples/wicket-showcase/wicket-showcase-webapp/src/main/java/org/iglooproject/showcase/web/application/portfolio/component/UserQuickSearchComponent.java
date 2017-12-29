package org.iglooproject.showcase.web.application.portfolio.component;

import java.util.List;

import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.model.IModel;

import org.iglooproject.showcase.core.business.user.model.User;
import org.iglooproject.showcase.core.business.user.search.IUserSearchQuery;
import org.iglooproject.showcase.web.application.portfolio.page.UserDescriptionPage;
import org.iglooproject.wicket.more.markup.html.form.AbstractQuickSearchComponent;
import org.iglooproject.wicket.more.model.GenericEntityModel;

public class UserQuickSearchComponent extends AbstractQuickSearchComponent<User> {

	private static final long serialVersionUID = -7717935272455937918L;

	private static final UserChoiceRenderer USER_CHOICE_RENDERER = new UserChoiceRenderer();
	
	public UserQuickSearchComponent(String id) {
		this(id, new GenericEntityModel<Long, User>());
	}

	private UserQuickSearchComponent(String id, IModel<User> userModel) {
		super(id, userModel, USER_CHOICE_RENDERER, UserDescriptionPage.linkDescriptor(userModel));
	}

	@Override
	protected List<User> searchAutocomplete(String term, int limit, int offset) {
		return getBean(IUserSearchQuery.class).nameAutocomplete(term).list(offset, limit);
	}

	private static final class UserChoiceRenderer extends ChoiceRenderer<User> {
		private static final long serialVersionUID = 1L;
		
		@Override
		public Object getDisplayValue(User user) {
			return user != null ? user.getFullName() : "";
		}
		
		@Override
		public String getIdValue(User user, int index) {
			return user != null ? user.getId().toString() : "-1";
		}
	}
}
