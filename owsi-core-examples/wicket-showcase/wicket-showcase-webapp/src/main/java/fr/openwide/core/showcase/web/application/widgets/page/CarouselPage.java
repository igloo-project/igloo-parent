package fr.openwide.core.showcase.web.application.widgets.page;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import fr.openwide.core.showcase.web.application.widgets.component.ImageCarouselPanel;
import fr.openwide.core.wicket.more.markup.html.template.model.BreadCrumbElement;

public class CarouselPage extends WidgetsTemplate {

	private static final long serialVersionUID = -1589558461083310335L;

	public CarouselPage(PageParameters parameters) {
		super(parameters);
		
		addBreadCrumbElement(new BreadCrumbElement(new ResourceModel("widgets.menu.carousel"), CarouselPage.class));
		
		add(new ImageCarouselPanel("carouselPanel", "dummy-diapo"));
	}

	@Override
	protected Class<? extends WebPage> getSecondMenuPage() {
		return CarouselPage.class;
	}
}
