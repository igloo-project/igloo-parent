package fr.openwide.core.basicapp.web.application.administration.page;

import java.util.List;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.odlabs.wiquery.core.events.MouseEvent;

import fr.openwide.core.basicapp.core.business.user.model.UserGroup;
import fr.openwide.core.basicapp.core.business.user.service.IUserGroupService;
import fr.openwide.core.basicapp.core.config.application.BasicApplicationConfigurer;
import fr.openwide.core.basicapp.web.application.administration.component.UserGroupPortfolioPanel;
import fr.openwide.core.basicapp.web.application.administration.form.UserGroupPopup;
import fr.openwide.core.basicapp.web.application.administration.template.AdministrationTemplate;
import fr.openwide.core.wicket.more.link.descriptor.IPageLinkDescriptor;
import fr.openwide.core.wicket.more.link.descriptor.builder.LinkDescriptorBuilder;
import fr.openwide.core.wicket.more.markup.html.link.BlankLink;
import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.bootstrap.modal.behavior.AjaxModalOpenBehavior;
import fr.openwide.core.wicket.more.markup.html.template.model.BreadCrumbElement;

public class AdministrationUserGroupPortfolioPage extends AdministrationTemplate {

	private static final long serialVersionUID = 2733071974944289365L;

	@SpringBean
	private BasicApplicationConfigurer configurer;

	@SpringBean
	private IUserGroupService userGroupService;

	public AdministrationUserGroupPortfolioPage(PageParameters parameters) {
		super(parameters);
		
		addBreadCrumbElement(new BreadCrumbElement(new ResourceModel("navigation.administration.usergroup"),
				AdministrationUserGroupPortfolioPage.linkDescriptor()));
		
		IModel<List<UserGroup>> userGroupListModel = new LoadableDetachableModel<List<UserGroup>>() {
			private static final long serialVersionUID = 1L;
			@Override
			protected List<UserGroup> load() {
				return userGroupService.list();
			}
		};
		
		UserGroupPopup addPopup = new UserGroupPopup("addPopup");
		
		add(
				new UserGroupPortfolioPanel("portfolio", userGroupListModel, configurer.getPortfolioItemsPerPage()),
				
				addPopup,
				new BlankLink("addButton")
						.add(new AjaxModalOpenBehavior(addPopup, MouseEvent.CLICK))
		);
	}

	public static IPageLinkDescriptor linkDescriptor() {
		return new LinkDescriptorBuilder()
				.page(AdministrationUserGroupPortfolioPage.class)
				.build();
	}

	@Override
	protected Class<? extends WebPage> getSecondMenuPage() {
		return AdministrationUserGroupPortfolioPage.class;
	}
}
