package fr.openwide.core.showcase.web.application.portfolio.component;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.SubmitLink;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.navigation.paging.IPageable;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import fr.openwide.core.jpa.security.business.authority.service.IAuthorityService;
import fr.openwide.core.showcase.core.business.user.model.User;
import fr.openwide.core.showcase.web.application.portfolio.page.UserDescriptionPage;
import fr.openwide.core.showcase.web.application.widgets.component.UserAutocompleteAjaxComponent;

public class UserSearchPanel extends Panel {
	
	private static final long serialVersionUID = -6224313886789870489L;
	
	@SpringBean
	IAuthorityService authorityService;
	
	private IPageable pageable;
	
	private IModel<String> searchTermModel;
	private IModel<User> userModel;
	private IModel<Boolean> activeModel;
	
	public UserSearchPanel(String id, IPageable pageable, IModel<String> searchTermModel,
			IModel<User> userModel, IModel<Boolean> activeModel) {
		super(id);
		
		this.pageable = pageable;
		
		this.searchTermModel = searchTermModel;
		this.userModel = userModel;
		this.activeModel = activeModel;
		
		UserAutocompleteAjaxComponent userQuickSearch = new UserAutocompleteAjaxComponent("userQuickSearch", this.userModel) {
			private static final long serialVersionUID = 9005792964233363514L;
			{
				setAutoUpdate(true);
			}
			
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				if (getModelObject() != null) {
					UserDescriptionPage.linkDescriptor(getModel()).setResponsePage();
					return;
				}
			}
		};
		add(userQuickSearch);
		
		Form<Void> form = new Form<Void>("form") {
			private static final long serialVersionUID = 3070604877946816317L;
			@Override
			protected void onSubmit() {
				// Lors de la soumission d'un formulaire de recherche, on retourne sur la première page
				UserSearchPanel.this.pageable.setCurrentPage(0);
				super.onSubmit();
			}
		};
		
		TextField<String> searchInput = new TextField<String>("searchInput", this.searchTermModel);
		searchInput.setLabel(new ResourceModel("user.portfolio.search.name"));
		form.add(searchInput);
		
		CheckBox active = new CheckBox("active", this.activeModel);
		active.setLabel(new ResourceModel("user.portfolio.header.active"));
		form.add(active);
		
		form.add(new SubmitLink("submit"));
		
		add(form);
	}

	@Override
	protected void onDetach() {
		super.onDetach();
		if (this.searchTermModel != null) {
			this.searchTermModel.detach();
		}
		if (this.activeModel != null) {
			this.activeModel.detach();
		}
	}
}
