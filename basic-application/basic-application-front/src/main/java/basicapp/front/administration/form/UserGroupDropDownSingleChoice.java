package basicapp.front.administration.form;

import java.util.Collection;
import java.util.Map;

import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.iglooproject.jpa.more.business.sort.ISort.SortOrder;
import org.iglooproject.wicket.more.markup.html.select2.GenericSelect2DropDownSingleChoice;

import com.google.common.collect.ImmutableMap;

import basicapp.back.business.user.model.UserGroup;
import basicapp.back.business.user.search.IUserGroupSearchQuery;
import basicapp.back.business.user.search.UserGroupSearchQueryData;
import basicapp.back.business.user.search.UserGroupSort;
import basicapp.front.common.form.impl.UserGroupChoiceRenderer;

public class UserGroupDropDownSingleChoice extends GenericSelect2DropDownSingleChoice<UserGroup> {

	private static final long serialVersionUID = 8845987968858565378L;

	public UserGroupDropDownSingleChoice(String id, IModel<UserGroup> model) {
		super(id, model, new ChoicesModel(), UserGroupChoiceRenderer.get());
	}

	private static class ChoicesModel extends LoadableDetachableModel<Collection<UserGroup>> {
		
		private static final long serialVersionUID = 1L;
		
		@SpringBean
		private IUserGroupSearchQuery searchQuery;
		
		public ChoicesModel() {
			super();
			Injector.get().inject(this);
		}
		
		@Override
		protected Collection<UserGroup> load() {
			Map<UserGroupSort, SortOrder> sorts = ImmutableMap.of(
				UserGroupSort.SCORE, UserGroupSort.SCORE.getDefaultOrder(),
				UserGroupSort.NAME, UserGroupSort.NAME.getDefaultOrder(),
				UserGroupSort.ID, UserGroupSort.ID.getDefaultOrder()
			);
			return searchQuery.list(new UserGroupSearchQueryData(), sorts, 0, Integer.MAX_VALUE);
		}
	}

}
