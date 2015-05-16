package fr.openwide.core.showcase.web.application.portfolio.component;

import java.util.List;

import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import fr.openwide.core.showcase.core.business.user.model.User;
import fr.openwide.core.showcase.core.business.user.search.IUserSearchQuery;
import fr.openwide.core.showcase.web.application.portfolio.page.UserDescriptionPage;
import fr.openwide.core.wicket.more.application.CoreWicketApplication;
import fr.openwide.core.wicket.more.markup.html.form.AbstractQuickSearchComponent;
import fr.openwide.core.wicket.more.model.GenericEntityModel;

public class UserQuickSearchComponent extends AbstractQuickSearchComponent<User> {

	private static final long serialVersionUID = -7717935272455937918L;

	private static final UserChoiceRenderer USER_CHOICE_RENDERER = new UserChoiceRenderer();
	
	@SpringBean
	private IUserSearchQuery userSearchQuery;

	public UserQuickSearchComponent(String id) {
		this(id, new GenericEntityModel<Long, User>());
	}

	private UserQuickSearchComponent(String id, IModel<User> userModel) {
		super(id, userModel, USER_CHOICE_RENDERER, UserDescriptionPage.linkDescriptor(userModel));
	}

	@Override
	protected List<User> searchAutocomplete(String term, int limit, int offset) {
		return CoreWicketApplication.get().getApplicationContext().getBean(IUserSearchQuery.class)
				.nameAutocomplete(term).list(offset, limit);
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
