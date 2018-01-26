package org.iglooproject.basicapp.web.application.administration.component;

import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.navigation.paging.IPageable;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.ResourceModel;
import org.iglooproject.basicapp.core.business.user.model.User;
import org.iglooproject.basicapp.web.application.administration.form.UserGroupDropDownSingleChoice;
import org.iglooproject.basicapp.web.application.administration.model.AbstractUserDataProvider;
import org.iglooproject.basicapp.web.application.common.form.UserQuickSearchComponent;
import org.iglooproject.basicapp.web.application.common.typedescriptor.user.UserTypeDescriptor;
import org.iglooproject.wicket.markup.html.form.PageableSearchForm;
import org.iglooproject.wicket.more.markup.html.form.LabelPlaceholderBehavior;

public class UserSearchPanel<U extends User> extends Panel {
	
	private static final long serialVersionUID = -6224313886789870489L;
	
	public UserSearchPanel(String id, IPageable pageable, UserTypeDescriptor<U> typeDescriptor,
			AbstractUserDataProvider<U> dataProvider) {
		super(id);
		
		UserQuickSearchComponent<?> userQuickSearch = new UserQuickSearchComponent<>("userQuickSearch", typeDescriptor);
		userQuickSearch.setAutoUpdate(true);
		userQuickSearch.getAutocompleteField().setLabel(new ResourceModel("common.quickAccess"));
		userQuickSearch.getAutocompleteField().add(new LabelPlaceholderBehavior());
		
		add(
				new PageableSearchForm<Void>("form", pageable)
						.add(
								new TextField<String>("name", dataProvider.getNameModel())
										.setLabel(new ResourceModel("administration.user.search.name"))
										.add(new LabelPlaceholderBehavior()),
								new UserGroupDropDownSingleChoice("userGroup", dataProvider.getGroupModel())
										.setLabel(new ResourceModel("administration.user.search.group"))
										.add(new LabelPlaceholderBehavior()),
								new CheckBox("active", dataProvider.getIncludeInactivesModel())
										.setLabel(new ResourceModel("administration.user.search.includeInactives"))
										.setOutputMarkupId(true),
								userQuickSearch
						)
		);
	}

}
