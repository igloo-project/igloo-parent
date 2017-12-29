package org.iglooproject.showcase.web.application.links.page;

import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import org.iglooproject.showcase.core.business.user.model.User;
import org.iglooproject.showcase.web.application.links.component.DynamicImageTestPanel;
import org.iglooproject.showcase.web.application.links.component.DynamicLinkTestPanel;
import org.iglooproject.showcase.web.application.util.template.MainTemplate;
import org.iglooproject.wicket.more.condition.Condition;
import org.iglooproject.wicket.more.link.descriptor.IPageLinkDescriptor;
import org.iglooproject.wicket.more.link.descriptor.builder.LinkDescriptorBuilder;
import org.iglooproject.wicket.more.link.descriptor.parameter.CommonParameters;
import org.iglooproject.wicket.more.link.descriptor.parameter.validator.LinkParameterValidationException;
import org.iglooproject.wicket.more.model.GenericEntityModel;

public abstract class LinksTemplate extends MainTemplate {
	
	private static final long serialVersionUID = -2979443021509594346L;
	
	public static final IPageLinkDescriptor linkDescriptor(IModel<? extends Class<? extends Page>> pageModel, IModel<User> userModel) {
		return LinkDescriptorBuilder.start()
				.map(CommonParameters.ID, userModel, User.class).mandatory()
				.page(pageModel);
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
		
		Component linkToPage1 = LinkDescriptorBuilder.toPageInstance(this).validate(LinksPage1.class).build()
				.link("linkToThisPageInstanceOnlyIfPage1").hideIfInvalid();
		add(linkToPage1);
		
		add(LinkDescriptorBuilder.toPageInstance(this).validate(LinksTemplate.class).build().link("linkToThisPageInstance")
				.add(Condition.componentVisible(linkToPage1).thenHide()));
		
		add(new DynamicLinkTestPanel("linkTestPanel", userModel));
		
		add(new DynamicImageTestPanel("imageTestPanel"));
	}

	@Override
	protected Class<? extends WebPage> getFirstMenuPage() {
		return LinksPage1.class;
	}
	
	protected abstract IModel<String> getTitleModel();

}
