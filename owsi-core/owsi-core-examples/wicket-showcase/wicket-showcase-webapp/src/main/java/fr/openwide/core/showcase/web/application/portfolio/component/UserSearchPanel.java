package fr.openwide.core.showcase.web.application.portfolio.component;

import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.navigation.paging.IPageable;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.ResourceModel;

import fr.openwide.core.showcase.core.business.user.model.User;
import fr.openwide.core.showcase.web.application.portfolio.model.AbstractUserDataProvider;
import fr.openwide.core.wicket.markup.html.form.PageableSearchForm;
import fr.openwide.core.wicket.more.markup.html.form.LabelPlaceholderBehavior;

public class UserSearchPanel extends Panel {
	
	private static final long serialVersionUID = -6224313886789870489L;
	
	public UserSearchPanel(String id, IPageable pageable, AbstractUserDataProvider<User> dataProvider) {
		super(id);
		
		// Quick search
		add(new UserQuickSearchComponent("userQuickSearch"));
		
		// Search form
		Form<Void> form = new PageableSearchForm<Void>("form", pageable);
		
		TextField<String> searchInput = new TextField<String>("searchInput", dataProvider.getNameModel());
		searchInput.setLabel(new ResourceModel("user.portfolio.search.name"));
		searchInput.add(new LabelPlaceholderBehavior());
		form.add(searchInput);
		
		CheckBox active = new CheckBox("includeInactive", dataProvider.getIncludeInactivesModel());
		active.setLabel(new ResourceModel("user.portfolio.search.includeInactive"));
		form.add(active);
		
		add(form);
	}

}
