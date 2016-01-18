package fr.openwide.core.basicapp.web.application.referential.page;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import fr.openwide.core.basicapp.web.application.common.component.NavTabsPanel;
import fr.openwide.core.basicapp.web.application.referential.component.CityListPanel;
import fr.openwide.core.basicapp.web.application.referential.template.ReferentialTemplate;
import fr.openwide.core.wicket.more.link.descriptor.IPageLinkDescriptor;
import fr.openwide.core.wicket.more.link.descriptor.builder.LinkDescriptorBuilder;

public class ReferentialPage extends ReferentialTemplate {

	private static final long serialVersionUID = -4381694964311714573L;

	public static final IPageLinkDescriptor linkDescriptor() {
		return new LinkDescriptorBuilder().page(ReferentialPage.class)
				.build();
	}

	public ReferentialPage(PageParameters parameters) {
		super(parameters);
		
		add(new Label("pageTitle", pageTitleModel));
		
		add(new NavTabsPanel("tabs")
				.add(
						new NavTabsPanel.SimpleTabFactory("business.city") {
							@Override
							public Component createContent(String wicketId) {
								return new CityListPanel(wicketId);
							}
						}
				)
		);
	}

	@Override
	protected void onDetach() {
		super.onDetach();
		pageTitleModel.detach();
	}

	@Override
	protected Class<? extends WebPage> getSecondMenuPage() {
		// TODO Auto-generated method stub
		return null;
	}
}
