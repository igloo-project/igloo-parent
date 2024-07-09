package basicapp.front.administration.form;

import java.util.Collection;
import java.util.Map;

import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.iglooproject.functional.SerializableSupplier2;
import org.iglooproject.jpa.more.business.generic.model.search.EnabledFilter;
import org.iglooproject.jpa.more.business.sort.ISort.SortOrder;
import org.iglooproject.wicket.more.markup.html.select2.AbstractLongIdGenericEntityChoiceProvider;
import org.iglooproject.wicket.more.markup.html.select2.GenericSelect2AjaxDropDownMultipleChoice;
import org.wicketstuff.select2.Response;

import com.google.common.collect.ImmutableMap;

import basicapp.back.business.user.model.User;
import basicapp.back.business.user.search.IUserSearchQuery;
import basicapp.back.business.user.search.UserSearchQueryData;
import basicapp.back.business.user.search.UserSort;
import basicapp.front.common.renderer.UserRenderer;

public class UserAjaxDropDownMultipleChoice<C extends Collection<User>> extends GenericSelect2AjaxDropDownMultipleChoice<User> {

	private static final long serialVersionUID = 7076114890845943476L;

	public UserAjaxDropDownMultipleChoice(String id, IModel<C> model, SerializableSupplier2<? extends C> collectionSupplier) {
		this(id, model, collectionSupplier, new ChoiceProvider());
	}

	public UserAjaxDropDownMultipleChoice(String id, IModel<C> model, SerializableSupplier2<? extends C> collectionSupplier, org.wicketstuff.select2.ChoiceProvider<User> choiceProvider) {
		super(id, model, collectionSupplier, choiceProvider);
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
