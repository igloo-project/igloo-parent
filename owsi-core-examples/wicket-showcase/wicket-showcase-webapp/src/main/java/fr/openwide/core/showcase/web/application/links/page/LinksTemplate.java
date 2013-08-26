package fr.openwide.core.showcase.web.application.links.page;

import java.util.List;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.google.common.collect.Lists;

import fr.openwide.core.showcase.core.business.user.model.User;
import fr.openwide.core.showcase.web.application.links.component.LinksTestPanel;
import fr.openwide.core.showcase.web.application.util.template.MainTemplate;
import fr.openwide.core.wicket.more.link.descriptor.IPageLinkDescriptor;
import fr.openwide.core.wicket.more.link.descriptor.builder.LinkDescriptorBuilder;
import fr.openwide.core.wicket.more.link.descriptor.parameter.CommonParameters;
import fr.openwide.core.wicket.more.link.descriptor.parameter.validator.LinkParameterValidationException;
import fr.openwide.core.wicket.more.markup.html.template.model.NavigationMenuItem;
import fr.openwide.core.wicket.more.model.GenericEntityModel;

public abstract class LinksTemplate extends MainTemplate {
	
	private static final long serialVersionUID = -2979443021509594346L;
	
	public static IPageLinkDescriptor linkDescriptor(IModel<? extends Class<? extends Page>> pageModel, IModel<User> userModel) {
		return new LinkDescriptorBuilder()
				.page(pageModel)
				.map(CommonParameters.ID, userModel, User.class).mandatory()
				.build();
	}

	public LinksTemplate(PageParameters parameters) {
		super(parameters);
		
		IModel<User> userModel = new GenericEntityModel<Long, User>(null);
		try {
			linkDescriptor(new Model<Class<Page>>(null), userModel).extract(parameters);
		} catch (LinkParameterValidationException ignored) {
			// Get around the parameter validation for the purpose of this test
		}
		
		add(new Label("title", getTitleModel()));
		
		add(new LinksTestPanel("testPanel", userModel));
	}

	@Override
	protected List<NavigationMenuItem> getSubNav() {
		return Lists.newArrayList(
				LinksPage1.linkDescriptor().navigationMenuItem(new ResourceModel("links.menu.page1")),
				LinksPage2.linkDescriptor().navigationMenuItem(new ResourceModel("links.menu.page2")),
				LinksPage3.linkDescriptor().navigationMenuItem(new ResourceModel("links.menu.page3"))
		);
	}

	@Override
	protected Class<? extends WebPage> getFirstMenuPage() {
		return LinksPage1.class;
	}
	
	protected abstract IModel<String> getTitleModel();

}
