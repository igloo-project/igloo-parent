package org.iglooproject.showcase.web.application.widgets.component;

import org.apache.wicket.injection.Injector;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.iglooproject.showcase.core.business.user.model.User;
import org.iglooproject.showcase.core.business.user.search.IUserSearchQuery;
import org.iglooproject.showcase.core.business.user.service.IUserService;
import org.iglooproject.wicket.more.markup.html.select2.AbstractLongIdGenericEntityChoiceProvider;
import org.wicketstuff.select2.Response;

public class UserSelect2AjaxAdapter extends AbstractLongIdGenericEntityChoiceProvider<User> {

	private static final long serialVersionUID = 4710983767659627112L;
	
	private static final UserChoiceRenderer CHOICE_RENDERER = new UserChoiceRenderer();
	
	@SpringBean
	private IUserService userService;
	
	@SpringBean
	private IUserSearchQuery userSearchQuery;
	
	public UserSelect2AjaxAdapter() {
		super(User.class, CHOICE_RENDERER);
		Injector.get().inject(this);
	}

	@Override
	public void query(String term, int page, Response<User> response) {
		userSearchQuery.nameAutocomplete(term).fullList();
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
