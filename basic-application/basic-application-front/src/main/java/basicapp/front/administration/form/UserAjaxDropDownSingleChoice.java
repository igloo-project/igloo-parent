package basicapp.front.administration.form;

import java.util.Map;

import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.iglooproject.jpa.more.business.generic.model.search.EnabledFilter;
import org.iglooproject.jpa.more.business.sort.ISort.SortOrder;
import org.iglooproject.wicket.more.markup.html.select2.AbstractLongIdGenericEntityChoiceProvider;
import org.iglooproject.wicket.more.markup.html.select2.GenericSelect2AjaxDropDownSingleChoice;
import org.wicketstuff.select2.Response;

import com.google.common.collect.ImmutableMap;

import basicapp.back.business.user.model.User;
import basicapp.back.business.user.search.IUserSearchQuery;
import basicapp.back.business.user.search.UserSearchQueryData;
import basicapp.back.business.user.search.UserSort;
import basicapp.front.common.renderer.UserRenderer;

public class UserAjaxDropDownSingleChoice extends GenericSelect2AjaxDropDownSingleChoice<User> {

	private static final long serialVersionUID = 7076114890845943476L;

	public UserAjaxDropDownSingleChoice(String id, IModel<User> model) {
		this(id, model, new ChoiceProvider());
	}

	public UserAjaxDropDownSingleChoice(String id, IModel<User> model, org.wicketstuff.select2.ChoiceProvider<User> choiceProvider) {
		super(id, model, choiceProvider);
	}

	private static class ChoiceProvider extends AbstractLongIdGenericEntityChoiceProvider<User> {
		
		private static final long serialVersionUID = 1L;
		
		@SpringBean
		private IUserSearchQuery searchQuery;
		
		public ChoiceProvider() {
			super(User.class, UserRenderer.get());
			Injector.get().inject(this);
		}
		
		@Override
		protected void query(String term, int offset, int limit, Response<User> response) {
			UserSearchQueryData data = new UserSearchQueryData();
			data.setTerm(term);
			data.setEnabledFilter(EnabledFilter.ENABLED_ONLY);
			Map<UserSort, SortOrder> sorts = ImmutableMap.of(
				UserSort.SCORE, UserSort.SCORE.getDefaultOrder(),
				UserSort.LAST_NAME, UserSort.LAST_NAME.getDefaultOrder(),
				UserSort.FIRST_NAME, UserSort.FIRST_NAME.getDefaultOrder(),
				UserSort.ID, UserSort.ID.getDefaultOrder()
			);
			response.addAll(searchQuery.list(data, sorts, offset, limit));
		}
	}

}
