package org.iglooproject.showcase.web.application.widgets.page;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import org.iglooproject.showcase.web.application.widgets.component.ImageCarouselPanel;
import org.iglooproject.wicket.more.link.descriptor.IPageLinkDescriptor;
import org.iglooproject.wicket.more.link.descriptor.builder.LinkDescriptorBuilder;
import org.iglooproject.wicket.more.markup.html.template.model.BreadCrumbElement;

public class CarouselPage extends WidgetsTemplate {

	private static final long serialVersionUID = -1589558461083310335L;

	public static final IPageLinkDescriptor linkDescriptor() {
		return LinkDescriptorBuilder.start()
				.page(CarouselPage.class);
	}

	public CarouselPage(PageParameters parameters) {
		super(parameters);
		
		addBreadCrumbElement(new BreadCrumbElement(new ResourceModel("widgets.menu.carousel"), CarouselPage.linkDescriptor()));
		
		add(new ImageCarouselPanel("carouselPanel", "dummy-diapo"));
	}

	@Override
	protected Class<? extends WebPage> getSecondMenuPage() {
		return CarouselPage.class;
	}
}
