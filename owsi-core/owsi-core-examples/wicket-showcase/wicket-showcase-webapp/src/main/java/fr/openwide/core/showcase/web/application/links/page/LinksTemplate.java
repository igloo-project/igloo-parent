package fr.openwide.core.showcase.web.application.links.page;

import java.util.List;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.google.common.collect.Lists;

import fr.openwide.core.showcase.core.business.user.model.User;
import fr.openwide.core.showcase.core.business.user.service.IUserService;
import fr.openwide.core.showcase.web.application.links.component.LinksTestPanel;
import fr.openwide.core.showcase.web.application.util.template.MainTemplate;
import fr.openwide.core.wicket.more.link.utils.CoreLinkParameterUtils;
import fr.openwide.core.wicket.more.markup.html.template.model.NavigationMenuItem;

public abstract class LinksTemplate extends MainTemplate {
	
	private static final long serialVersionUID = -2979443021509594346L;
	
	@SpringBean
	private IUserService userService;

	public LinksTemplate(PageParameters parameters) {
		super(parameters);
		
		IModel<User> userModel = CoreLinkParameterUtils.extractGenericEntityParameterModel(userService, parameters, Long.class);
		
		add(new Label("title", getTitleModel()));
		
		add(new LinksTestPanel("testPanel", userModel));
	}

	@Override
	protected List<NavigationMenuItem> getSubNav() {
		return Lists.newArrayList(
				new NavigationMenuItem(new ResourceModel("links.menu.page1"), LinksPage1.class),
				new NavigationMenuItem(new ResourceModel("links.menu.page2"), LinksPage2.class),
				new NavigationMenuItem(new ResourceModel("links.menu.page3"), LinksPage3.class)
		);
	}

	@Override
	protected Class<? extends WebPage> getFirstMenuPage() {
		return LinksPage1.class;
	}
	
	protected abstract IModel<String> getTitleModel();

}
