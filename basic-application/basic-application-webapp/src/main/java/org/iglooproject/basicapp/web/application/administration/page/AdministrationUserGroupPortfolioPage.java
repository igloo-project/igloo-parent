package org.iglooproject.basicapp.web.application.administration.page;

import static org.iglooproject.basicapp.web.application.property.BasicApplicationWebappPropertyIds.PORTFOLIO_ITEMS_PER_PAGE;

import java.util.List;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.iglooproject.basicapp.core.business.user.model.UserGroup;
import org.iglooproject.basicapp.core.business.user.service.IUserGroupService;
import org.iglooproject.basicapp.web.application.administration.component.UserGroupPortfolioPanel;
import org.iglooproject.basicapp.web.application.administration.form.UserGroupPopup;
import org.iglooproject.basicapp.web.application.administration.template.AdministrationTemplate;
import org.iglooproject.spring.property.service.IPropertyService;
import org.iglooproject.wicket.more.link.descriptor.IPageLinkDescriptor;
import org.iglooproject.wicket.more.link.descriptor.builder.LinkDescriptorBuilder;
import org.iglooproject.wicket.more.markup.html.link.BlankLink;
import org.iglooproject.wicket.more.markup.html.template.js.bootstrap.modal.behavior.AjaxModalOpenBehavior;
import org.iglooproject.wicket.more.markup.html.template.model.BreadCrumbElement;
import org.wicketstuff.wiquery.core.events.MouseEvent;

public class AdministrationUserGroupPortfolioPage extends AdministrationTemplate {

	private static final long serialVersionUID = 2733071974944289365L;

	public static final IPageLinkDescriptor linkDescriptor() {
		return LinkDescriptorBuilder.start()
				.page(AdministrationUserGroupPortfolioPage.class);
	}

	@SpringBean
	private IPropertyService propertyService;

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
				new UserGroupPortfolioPanel("portfolio", userGroupListModel, propertyService.get(PORTFOLIO_ITEMS_PER_PAGE)),
				
				addPopup,
				new BlankLink("addButton")
						.add(new AjaxModalOpenBehavior(addPopup, MouseEvent.CLICK))
		);
	}

	@Override
	protected Class<? extends WebPage> getSecondMenuPage() {
		return AdministrationUserGroupPortfolioPage.class;
	}
}
