package fr.openwide.core.showcase.web.application.links.page;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import fr.openwide.core.showcase.core.business.user.service.IUserService;

public class LinksPage2 extends LinksTemplate {
	
	private static final long serialVersionUID = 2581363150311136728L;
	
	@SpringBean
	private IUserService userService;

	public LinksPage2(PageParameters parameters) {
		super(parameters);
	}

	@Override
	protected Class<? extends WebPage> getSecondMenuPage() {
		return LinksPage2.class;
	}

	@Override
	protected IModel<String> getTitleModel() {
		return new ResourceModel("links.page2.pageTitle");
	}

}
