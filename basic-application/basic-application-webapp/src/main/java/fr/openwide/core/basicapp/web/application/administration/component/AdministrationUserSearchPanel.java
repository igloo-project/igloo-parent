package fr.openwide.core.basicapp.web.application.administration.component;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.navigation.paging.IPageable;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;

import fr.openwide.core.basicapp.core.business.user.model.User;
import fr.openwide.core.basicapp.web.application.administration.page.AdministrationUserDescriptionPage;
import fr.openwide.core.basicapp.web.application.common.component.UserAutocompleteAjaxComponent;
import fr.openwide.core.wicket.more.link.model.PageModel;
import fr.openwide.core.wicket.more.markup.html.form.LabelPlaceholderBehavior;
import fr.openwide.core.wicket.more.model.GenericEntityModel;

public class AdministrationUserSearchPanel extends Panel {
	
	private static final long serialVersionUID = -6224313886789870489L;
	
	private IPageable pageable;
	
	private IModel<String> searchTermModel;
	
	private IModel<Boolean> activeModel;
	
	public AdministrationUserSearchPanel(String id, IPageable pageable, IModel<String> searchTermModel, IModel<Boolean> activeModel) {
		super(id);
		
		this.pageable = pageable;
		
		this.searchTermModel = searchTermModel;
		this.activeModel = activeModel;
		
		// Quick search
		UserAutocompleteAjaxComponent userQuickSearch = new UserAutocompleteAjaxComponent("userQuickSearch",
				new GenericEntityModel<Long, User>()) {
			private static final long serialVersionUID = 1L;
			
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				if (getModelObject() != null) {
					AdministrationUserDescriptionPage.linkDescriptor(getModel(), PageModel.of(getPage())).setResponsePage();
					return;
				}
			}
		};
		userQuickSearch.setAutoUpdate(true);
		userQuickSearch.getAutocompleteField().setLabel(new ResourceModel("common.quickAccess"));
		userQuickSearch.getAutocompleteField().add(new LabelPlaceholderBehavior());
		add(userQuickSearch);
		
		// Search form
		Form<Void> form = new Form<Void>("form") {
			private static final long serialVersionUID = 3070604877946816317L;
			@Override
			protected void onSubmit() {
				// Lors de la soumission d'un formulaire de recherche, on retourne sur la première page
				AdministrationUserSearchPanel.this.pageable.setCurrentPage(0);
				super.onSubmit();
			}
		};
		
		TextField<String> searchInput = new TextField<String>("searchInput", this.searchTermModel);
		searchInput.setLabel(new ResourceModel("administration.user.search.name"));
		searchInput.add(new LabelPlaceholderBehavior());
		form.add(searchInput);
		
		CheckBox active = new CheckBox("active", this.activeModel);
		active.setLabel(new ResourceModel("administration.user.field.active"));
		form.add(active);
		
		add(form);
	}

	@Override
	protected void onDetach() {
		super.onDetach();
		if (searchTermModel != null) {
			this.searchTermModel.detach();
		}
		if (this.activeModel != null) {
			this.activeModel.detach();
		}
	}
}
