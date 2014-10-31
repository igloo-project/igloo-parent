package fr.openwide.core.basicapp.web.application.common.form;

import java.util.List;

import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

import fr.openwide.core.basicapp.core.business.user.model.User;
import fr.openwide.core.basicapp.core.business.user.service.IUserService;
import fr.openwide.core.basicapp.web.application.administration.util.AdministrationUserTypeDescriptor;
import fr.openwide.core.wicket.more.model.GenericEntityModel;

public class UserQuickSearchComponent<U extends User> extends AbstractQuickSearchComponent<U> {

	private static final long serialVersionUID = -7717935272455937918L;

	private static final Logger LOGGER = LoggerFactory.getLogger(UserQuickSearchComponent.class);

	private static final UserChoiceRenderer USER_CHOICE_RENDERER = new UserChoiceRenderer();
	
	private final AdministrationUserTypeDescriptor<U> type;

	@SpringBean
	private IUserService userService;

	public UserQuickSearchComponent(String id, AdministrationUserTypeDescriptor<U> type) {
		this(id, new GenericEntityModel<Long, U>(), type);
	}

	private UserQuickSearchComponent(String id, IModel<U> userModel, AdministrationUserTypeDescriptor<U> type) {
		super(id, userModel, USER_CHOICE_RENDERER, type.fiche(userModel));
		this.type = type;
	}

	@Override
	protected List<U> searchAutocomplete(String term, int limit, int offset) {
		try {
			return userService.searchAutocomplete(type.getUserClass(), term, limit, offset);
		} catch (Exception e) {
			LOGGER.error("User autocomplete search error", e);
			return Lists.newArrayList();
		}
	}

	private static final class UserChoiceRenderer implements IChoiceRenderer<User> {
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
