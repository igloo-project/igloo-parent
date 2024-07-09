package basicapp.front.referencedata.page;

import org.apache.wicket.Component;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.iglooproject.wicket.more.link.descriptor.IPageLinkDescriptor;
import org.iglooproject.wicket.more.link.descriptor.builder.LinkDescriptorBuilder;

import basicapp.front.common.component.FeatureNotYetAvailablePanel;
import basicapp.front.common.component.NavTabsPanel;
import basicapp.front.referencedata.component.CityListPanel;
import basicapp.front.referencedata.template.ReferenceDataTemplate;
import igloo.wicket.component.CoreLabel;

public class ReferenceDataPage extends ReferenceDataTemplate {

	private static final long serialVersionUID = -4381694964311714573L;

	public static final IPageLinkDescriptor linkDescriptor() {
		return LinkDescriptorBuilder.start()
			.page(ReferenceDataPage.class);
	}

	public ReferenceDataPage(PageParameters parameters) {
		super(parameters);
		
		add(new CoreLabel("pageTitle", new ResourceModel("navigation.referenceData")));
		
		add(
			new NavTabsPanel("tabs")
				.add(
					new NavTabsPanel.SimpleTabFactory("city", "business.city") {
						private static final long serialVersionUID = 1L;
						@Override
						public Component createContent(String wicketId) {
							return new CityListPanel(wicketId);
						}
					}
				)
				.add(
					new NavTabsPanel.SimpleTabFactory("reference-data-2", Model.of("Item #2")) {
						private static final long serialVersionUID = 1L;
						@Override
						public Component createContent(String wicketId) {
							return new FeatureNotYetAvailablePanel(wicketId);
						}
					}
				)
				.add(
					new NavTabsPanel.SimpleTabFactory("reference-data-3", Model.of("Item #3")) {
						private static final long serialVersionUID = 1L;
						@Override
						public Component createContent(String wicketId) {
							return new FeatureNotYetAvailablePanel(wicketId);
						}
					}
				)
				.add(
					new NavTabsPanel.SimpleTabFactory("reference-data-4", Model.of("Item #4")) {
						private static final long serialVersionUID = 1L;
						@Override
						public Component createContent(String wicketId) {
							return new FeatureNotYetAvailablePanel(wicketId);
						}
					}
				)
		);
	}

}
