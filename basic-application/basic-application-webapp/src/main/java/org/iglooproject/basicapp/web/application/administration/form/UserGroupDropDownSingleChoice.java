package org.iglooproject.basicapp.web.application.administration.form;

import java.util.Collection;
import java.util.Map;

import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.iglooproject.basicapp.core.business.user.model.UserGroup;
import org.iglooproject.basicapp.core.business.user.search.IUserGroupSearchQuery;
import org.iglooproject.basicapp.core.business.user.search.UserGroupSearchQueryData;
import org.iglooproject.basicapp.core.business.user.search.UserGroupSort;
import org.iglooproject.basicapp.web.application.common.form.impl.UserGroupChoiceRenderer;
import org.iglooproject.jpa.more.business.sort.ISort.SortOrder;
import org.iglooproject.wicket.more.markup.html.select2.GenericSelect2DropDownSingleChoice;

import com.google.common.collect.ImmutableMap;

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
