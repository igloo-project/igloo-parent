package fr.openwide.core.showcase.web.application.widgets.page;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.model.util.ListModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import fr.openwide.core.showcase.core.business.user.model.User;
import fr.openwide.core.showcase.web.application.widgets.component.UserAutocompleteAjaxComponent;
import fr.openwide.core.showcase.web.application.widgets.component.UserItemItField;
import fr.openwide.core.wicket.more.link.descriptor.IPageLinkDescriptor;
import fr.openwide.core.wicket.more.link.descriptor.builder.LinkDescriptorBuilder;
import fr.openwide.core.wicket.more.markup.html.template.model.BreadCrumbElement;

public class AutocompletePage extends WidgetsTemplate {
	private static final long serialVersionUID = 1019469897091555748L;
	
	public static IPageLinkDescriptor linkDescriptor() {
		return new LinkDescriptorBuilder()
				.page(AutocompletePage.class)
				.build();
	}

	public AutocompletePage(PageParameters parameters) {
		super(parameters);
		
		addBreadCrumbElement(new BreadCrumbElement(new ResourceModel("widgets.menu.autocomplete"), AutocompletePage.linkDescriptor()));
		
		UserItemItField userItemItField = new UserItemItField("userItemItField", new ListModel<User>());
		userItemItField.setLabel(new ResourceModel("widgets.autocomplete.userItemIt"));
		add(userItemItField);
		
		add(new UserAutocompleteAjaxComponent("userAutocompleteAjaxComponent"));
	}
	
	@Override
	protected Class<? extends WebPage> getSecondMenuPage() {
		return AutocompletePage.class;
	}
}
